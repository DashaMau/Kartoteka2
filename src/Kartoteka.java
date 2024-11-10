import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Kartoteka {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Student> kartoteka = new List<>();

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить студента");
            System.out.println("2. Удалить студента");
            System.out.println("3. Вывести всех студентов");
            System.out.println("4. Очистить картотеку");
            System.out.println("5. Проверить картотеку на пустоту");
            System.out.println("6. Сохранить в файл");
            System.out.println("7. Загрузить из файла");
            System.out.println("0. Выход");
            System.out.print("Введите номер операции: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Считываем символ новой строки

            switch (choice) {
                case 1:
                    Student student = new Student();
                    student.считатьСКонсоли(scanner);
                    kartoteka.добавить(student);
                    break;
                case 2:
                    if (kartoteka.isEmpty()) {
                        System.out.println("Картотека пуста!");
                    } else {
                        System.out.println("Введите имя студента для удаления: ");
                        String nameToDelete = scanner.nextLine();
                        kartoteka.удалить(nameToDelete);
                    }
                    break;
                case 3:
                    kartoteka.вывести();
                    break;
                case 4:
                    kartoteka.очистить();
                    System.out.println("Картотека очищена.");
                    break;
                case 5:
                    if (kartoteka.isEmpty()) {
                        System.out.println("Картотека пуста.");
                    } else {
                        System.out.println("Картотека не пуста.");
                    }
                    break;
                case 6:
                    kartoteka.записатьВФайл("kartoteka.txt");
                    break;
                case 7:
                    kartoteka.читатьИзФайла("kartoteka.txt");
                    break;
                case 0:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор операции.");
            }
        }
    }
}

class Student {
    String имя;
    int возраст;

    public void считатьСКонсоли(Scanner scanner) {
        System.out.print("Введите имя: ");
        имя = scanner.nextLine();
        System.out.print("Введите возраст: ");
        возраст = scanner.nextInt();
    }

    public void вывести() {
        System.out.println("Имя: " + имя + ", Возраст: " + возраст);
    }
}

class Узел<T> {
    T data;
    Узел<T> prev;
    Узел<T> next;

    public Узел(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}

class List<T> {
    private Узел<T> head;
    private Узел<T> tail;

    public List() {
        this.head = null;
        this.tail = null;
    }

    public void добавить(T data) {
        Узел<T> newNode = new Узел<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public void удалить(String имя) {
        if (head == null) {
            return;
        }
        if (head.data instanceof Student && ((Student) head.data).имя.equals(имя)) {
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
            return;
        }
        Узел<T> current = head;
        while (current.next != null) {
            if (current.next.data instanceof Student && ((Student) current.next.data).имя.equals(имя)) {
                current.next = current.next.next;
                if (current.next != null) {
                    current.next.prev = current;
                } else {
                    tail = current;
                }
                return;
            }
            current = current.next;
        }
    }

    public void вывести() {
        if (head == null) {
            System.out.println("Картотека пуста.");
            return;
        }
        Узел<T> current = head;
        while (current != null) {
            if (current.data instanceof Student) {
                ((Student) current.data).вывести();
            }
            current = current.next;
        }
    }

    public void очистить() {
        head = null;
        tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void записатьВФайл(String filename) {
        try (FileWriter fileWriter = new FileWriter(new File(filename))) {
            if (head == null) {
                fileWriter.write("Картотека пуста.");
                return;
            }
            Узел<T> current = head;
            while (current != null) {
                if (current.data instanceof Student) {
                    fileWriter.write(((Student) current.data).имя + "," + ((Student) current.data).возраст + "\n");
                }
                current = current.next;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public void читатьИзФайла(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            очистить();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    Student student = new Student();
                    student.имя = parts[0];
                    student.возраст = Integer.parseInt(parts[1]);
                    добавить(student);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
        }
    }
}
