package carsharing;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    static Scanner scanner = new Scanner(System.in);
    private final String title;
    private final List<MenuItem> items = new ArrayList<>();
    private MenuItem exitAction;

    public Menu(String title) {
        this.title = title;
    }

    public Menu() {
        this("");
    }

    public void addExitAction(MenuItem item) {
        this.exitAction = item;
    }

    public void show() {
        if (!title.isBlank()) {
            System.out.println(title);
        }
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf("%d. %s%n", i + 1, item.getName());
        }
        if (exitAction != null) {
            System.out.printf("0. %s%n", exitAction.getName());
        }

        chooseItem().execute();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    private MenuItem chooseItem() {
        while (true) {
            String input = scanner.nextLine();
            if (!input.matches("\\d+")) {
                continue;
            }
            int index = Integer.parseInt(input);
            if (index == 0 && exitAction == null) {
                continue;
            }
            if (index < 0 || index > items.size()) {
                continue;
            }
            return index == 0 ? exitAction : items.get(index - 1);
        }
    }

}

class MenuItem {
    String name;
    Action action;

    public MenuItem(String name, Action action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void execute() {
        action.execute();
    }
}

interface Action {
    void execute();
}