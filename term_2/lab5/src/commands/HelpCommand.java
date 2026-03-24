package commands;

public class HelpCommand implements Command {
    private String args;
    @Override 
    public void setArgs(String args) {
         this.args = args; 
    }

    @Override public void execute() {
        System.out.println("Доступные команды:\n" +
                "help\ninsert\nupdate\nshow\nremove_key\nclear\nsave\nexecute_script\nexit\n" +
                "remove_lower\nreplace_if_greater\nremove_greater_key\n" +
                "count_greater_than_number_of_participants\nfilter_greater_than_genre\n" +
                "print_field_descending_front_man");
    }
}