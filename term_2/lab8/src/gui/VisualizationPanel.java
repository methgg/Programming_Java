package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import models.MusicBand;

public class VisualizationPanel extends JPanel {
    private LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();
    private Map<Long, String> ownerByKey = new LinkedHashMap<>();
    private Map<String, Color> colorByOwner = new LinkedHashMap<>();
    private Map<Long, String> signaturesByKey = new HashMap<>();
    private Map<Long, Double> animationProgressByKey = new HashMap<>();
    private Set<Long> animatingKeys = new HashSet<>();
    private Map.Entry<Long, MusicBand> selectedEntry;
    private Timer animationTimer;
    private Locale locale = GuiResources.EN_IN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    public VisualizationPanel() {
        setBackground(Color.WHITE);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedEntry = findEntryAt(e.getX(), e.getY());
                repaint();
            }
        });
    }

    public void setCollection(LinkedHashMap<Long, MusicBand> collection) {
        setCollection(collection, new LinkedHashMap<>());
    }

    public void setCollection(LinkedHashMap<Long, MusicBand> collection, Map<Long, String> ownerByKey) {
        Set<Long> changedKeys = findChangedKeys(collection, ownerByKey);
        this.collection = collection;
        this.ownerByKey = ownerByKey;
        rebuildOwnerColors();

        signaturesByKey.keySet().removeIf(key -> !collection.containsKey(key));
        animationProgressByKey.keySet().removeIf(key -> !collection.containsKey(key));

        for (Map.Entry<Long, MusicBand> entry : collection.entrySet()) {
            signaturesByKey.put(entry.getKey(), signature(entry.getKey(), entry.getValue(), ownerByKey));
        }

        if (selectedEntry != null && !collection.containsKey(selectedEntry.getKey())) {
            selectedEntry = null;
        }

        startPulseAnimation(changedKeys);
    }

    public Map.Entry<Long, MusicBand> getSelectedEntry() {
        return selectedEntry;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.numberFormat = NumberFormat.getNumberInstance(locale);
        repaint();
    }

    private Map.Entry<Long, MusicBand> findEntryAt(int mouseX, int mouseY) {
        Map<Long, Point> centers = calculateDisplayCenters();

        for (Map.Entry<Long, MusicBand> entry : collection.entrySet()) {
            Point center = centers.get(entry.getKey());
            if (center == null) {
                continue;
            }

            int centerX = center.x;
            int centerY = center.y;
            int radius = getRadius(entry.getValue());

            int dx = mouseX - centerX;
            int dy = mouseY - centerY;

            if (dx * dx + dy * dy <= radius * radius) {
                return entry;
            }
        }

        return null;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<Long, Point> centers = calculateDisplayCenters();

        for (Map.Entry<Long, MusicBand> entry : collection.entrySet()) {
            drawBand(g, entry, centers);
        }

        drawSelectedInfo(g);
    }

    private void drawBand(Graphics2D g, Map.Entry<Long, MusicBand> entry, Map<Long, Point> centers) {
        MusicBand band = entry.getValue();

        Point center = centers.get(entry.getKey());
        if (center == null) {
            return;
        }

        int centerX = center.x;
        int centerY = center.y;
        double progress = animationProgressByKey.getOrDefault(entry.getKey(), 1.0);
        int radius = Math.max(2, (int) Math.round(getRadius(band) * progress));

        g.setColor(colorForOwner(ownerByKey.get(entry.getKey())));
        g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        if (selectedEntry != null && selectedEntry.getKey().equals(entry.getKey())) {
            g.setColor(new Color(0, 220, 0));
            g.drawOval(centerX - radius - 3, centerY - radius - 3, radius * 2 + 6, radius * 2 + 6);
        }

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(entry.getKey()), centerX - 6, centerY + 4);
    }

    private void drawSelectedInfo(Graphics2D g) {
        if (selectedEntry == null) {
            return;
        }

        MusicBand band = selectedEntry.getValue();

        int x = 16;
        int y = getHeight() - 95;

        g.setColor(Color.BLACK);
        g.drawString(GuiResources.get(locale, "selected.object"), x, y);
        g.drawString(GuiResources.get(locale, "key") + ": " + numberFormat.format(selectedEntry.getKey()), x, y + 18);
        g.drawString("ID: " + numberFormat.format(band.getId()), x, y + 36);
        g.drawString(GuiResources.get(locale, "column.name") + ": " + band.getName(), x, y + 54);
        g.drawString(GuiResources.get(locale, "column.genre") + ": " + band.getGenre(), x, y + 72);
        g.drawString(GuiResources.get(locale, "owner") + ": "
                + ownerByKey.getOrDefault(selectedEntry.getKey(), "unknown"), x, y + 90);
    }

    private int toScreenX(MusicBand band) {
        int x = band.getCoordinates().getX();
        return Math.max(30, Math.min(getWidth() - 30, x));
    }

    private int toScreenY(MusicBand band) {
        int y = band.getCoordinates().getY().intValue();
        return Math.max(30, Math.min(getHeight() - 120, y));
    }

    private int getRadius(MusicBand band) {
        int participants = band.getNumberOfParticipants();
        return Math.max(12, Math.min(55, participants / 2));
    }

    private Map<Long, Point> calculateDisplayCenters() {
        Map<Long, Double> xs = new HashMap<>();
        Map<Long, Double> ys = new HashMap<>();

        for (Map.Entry<Long, MusicBand> entry : collection.entrySet()) {
            xs.put(entry.getKey(), (double) toScreenX(entry.getValue()));
            ys.put(entry.getKey(), (double) toScreenY(entry.getValue()));
        }

        Long[] keys = collection.keySet().toArray(new Long[0]);
        int gap = 8;

        for (int iteration = 0; iteration < 80; iteration++) {
            boolean moved = false;

            for (int i = 0; i < keys.length; i++) {
                for (int j = i + 1; j < keys.length; j++) {
                    Long firstKey = keys[i];
                    Long secondKey = keys[j];

                    MusicBand firstBand = collection.get(firstKey);
                    MusicBand secondBand = collection.get(secondKey);

                    double firstX = xs.get(firstKey);
                    double firstY = ys.get(firstKey);
                    double secondX = xs.get(secondKey);
                    double secondY = ys.get(secondKey);

                    double dx = secondX - firstX;
                    double dy = secondY - firstY;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double minDistance = getRadius(firstBand) + getRadius(secondBand) + gap;

                    if (distance < minDistance) {
                        if (distance < 0.001) {
                            double angle = 2 * Math.PI * (i + 1) / Math.max(1, keys.length);
                            dx = Math.cos(angle);
                            dy = Math.sin(angle);
                            distance = 1.0;
                        }

                        double push = (minDistance - distance) / 2.0;
                        double pushX = dx / distance * push;
                        double pushY = dy / distance * push;

                        xs.put(firstKey, firstX - pushX);
                        ys.put(firstKey, firstY - pushY);
                        xs.put(secondKey, secondX + pushX);
                        ys.put(secondKey, secondY + pushY);
                        moved = true;
                    }
                }
            }

            for (Long key : keys) {
                MusicBand band = collection.get(key);
                double anchorX = toScreenX(band);
                double anchorY = toScreenY(band);

                xs.put(key, xs.get(key) + (anchorX - xs.get(key)) * 0.02);
                ys.put(key, ys.get(key) + (anchorY - ys.get(key)) * 0.02);
            }

            keepCentersInside(xs, ys, keys);

            if (!moved) {
                break;
            }
        }

        Map<Long, Point> centers = new HashMap<>();
        for (Long key : keys) {
            centers.put(key, new Point(
                    (int) Math.round(xs.get(key)),
                    (int) Math.round(ys.get(key))
            ));
        }

        return centers;
    }

    private void keepCentersInside(Map<Long, Double> xs, Map<Long, Double> ys, Long[] keys) {
        for (Long key : keys) {
            MusicBand band = collection.get(key);
            int radius = getRadius(band);
            double minX = radius + 4;
            double maxX = Math.max(minX, getWidth() - radius - 4);
            double minY = radius + 4;
            double maxY = Math.max(minY, getHeight() - 120 - radius);

            xs.put(key, Math.max(minX, Math.min(maxX, xs.get(key))));
            ys.put(key, Math.max(minY, Math.min(maxY, ys.get(key))));
        }
    }

    private Color colorForOwner(String owner) {
        return colorByOwner.getOrDefault(String.valueOf(owner), new Color(120, 120, 120));
    }

    private void rebuildOwnerColors() {
        Color[] colors = ownerPalette();
        Map<String, Color> rebuiltColors = new LinkedHashMap<>();

        for (Long key : collection.keySet()) {
            String owner = String.valueOf(ownerByKey.getOrDefault(key, "unknown"));
            if (!rebuiltColors.containsKey(owner)) {
                rebuiltColors.put(owner, colors[rebuiltColors.size() % colors.length]);
            }
        }

        colorByOwner = rebuiltColors;
    }

    private Color[] ownerPalette() {
        return new Color[] {
                new Color(230, 80, 80),
                new Color(70, 150, 230),
                new Color(240, 180, 60),
                new Color(130, 90, 210),
                new Color(70, 180, 120),
                new Color(230, 110, 190),
                new Color(90, 190, 210),
                new Color(180, 170, 60)
        };
    }

    private Set<Long> findChangedKeys(LinkedHashMap<Long, MusicBand> newCollection, Map<Long, String> newOwnersByKey) {
        Set<Long> changedKeys = new HashSet<>();

        for (Map.Entry<Long, MusicBand> entry : newCollection.entrySet()) {
            String oldSignature = signaturesByKey.get(entry.getKey());
            String newSignature = signature(entry.getKey(), entry.getValue(), newOwnersByKey);

            if (!newSignature.equals(oldSignature)) {
                changedKeys.add(entry.getKey());
            }
        }

        return changedKeys;
    }

    private String signature(Long key, MusicBand band, Map<Long, String> ownersByKey) {
        return key + "|"
                + band.getId() + "|"
                + band.getName() + "|"
                + band.getCoordinates().getX() + "|"
                + band.getCoordinates().getY() + "|"
                + band.getNumberOfParticipants() + "|"
                + band.getGenre() + "|"
                + band.getFrontMan().getName() + "|"
                + band.getFrontMan().getBirthday() + "|"
                + band.getFrontMan().getHeight() + "|"
                + band.getFrontMan().getPassportID() + "|"
                + band.getFrontMan().getEyeColor() + "|"
                + ownersByKey.getOrDefault(key, "unknown");
    }

    private void startPulseAnimation(Set<Long> changedKeys) {
        if (changedKeys.isEmpty()) {
            repaint();
            return;
        }

        animatingKeys = new HashSet<>(changedKeys);

        for (Long key : changedKeys) {
            animationProgressByKey.put(key, 0.0);
        }

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(25, e -> {
            Set<Long> finishedKeys = new HashSet<>();

            for (Long key : animatingKeys) {
                double progress = animationProgressByKey.getOrDefault(key, 1.0) + 0.08;

                if (progress >= 1.0) {
                    progress = 1.0;
                    finishedKeys.add(key);
                }

                animationProgressByKey.put(key, progress);
            }

            animatingKeys.removeAll(finishedKeys);

            if (animatingKeys.isEmpty()) {
                animationTimer.stop();
            }

            repaint();
        });
        animationTimer.start();
    }
}
