package util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConsoleReader{
    public static String readLineWithTabCompletion(String prompt, Set<String> commands) throws IOException, InterruptedException{
        runStty("-icanon -echo"); // -icannon отключает построчный ввод, читаем посимвольно -echo отключает автовывод набранных символов терминалом
        StringBuilder buffer = new StringBuilder();
        System.out.print(prompt);
        System.out.flush();
        try {
            while (true) { 
                int ch = System.in.read();
                if (ch == -1) return null;
                if (ch == '\n' || ch == '\r') {
                    System.out.println();
                    return buffer.toString();
                }
                if (ch == 127 || ch == '\b') {
                    if (buffer.length() > 0) {
                        buffer.deleteCharAt(buffer.length() - 1);
                        redraw(prompt, buffer.toString());
                    }
                    continue;
                }
                if (ch == '\t') {
                    String current = buffer.toString();
                    String[] parts = current.split("\\s+", 2);
                    String commandPart = parts.length > 0 ? parts[0] : "";
                    List<String> matches = commands.stream().filter(c -> c.startsWith(commandPart)).sorted().toList();
                    if (matches.size() == 1) {
                        String full = matches.get(0);
                        buffer.setLength(0);
                        buffer.append(full);
                        if (parts.length > 1) buffer.append(" ").append(parts[1]);
                        redraw(prompt, buffer.toString());
                    } else if (matches.size() > 1) {
                        System.out.println();
                        for (String m : matches) System.out.println(m);
                        redraw(prompt, buffer.toString());
                    }
                    continue;
                }
                buffer.append((char) ch);
                redraw(prompt, buffer.toString());
            }} finally {
                runStty("sane");
            }
        }

    public static String readLine(String prompt) throws IOException, InterruptedException {
        return readLineWithTabCompletion(prompt, Set.of());
    }
    
    private static void redraw(String prompt, String text) {
        System.out.print("\r\033[2K" + prompt + text);
        System.out.flush();
    }

    private static void runStty(String args) throws IOException, InterruptedException {
        Process p = new ProcessBuilder("sh", "-c", "stty " + args + " < /dev/tty").start();
        if (p.waitFor() != 0) throw new IOException("stty failed: " + args);
    }
    }

    
