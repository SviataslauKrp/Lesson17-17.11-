import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Map<String, Document> map = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Введите IP-адресс");
        try (reader) {

            String ip = reader.readLine();
            Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
            Matcher matcher = pattern.matcher(ip);

            if (matcher.find()) {
                System.out.println("IP-адресс корректный");
            } else {
                System.out.println("IP-адресс некорректный");
            }
            System.out.println("Введите путь к файлам");
            String path = reader.readLine();
            Path pathToFiles;
            while (true) {
                pathToFiles = Paths.get(path);
                if (Files.isDirectory(pathToFiles)) {
                    break;
                } else {
                    System.out.println("Введите путь к файлам корректно");
                    path = reader.readLine();
                }
            }
            System.out.println("Введите количество файлов для прочтения");
            int numberOfFiles = 0;
            while (true) {
                try {
                    numberOfFiles = Integer.parseInt(reader.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Введите число:");
                }
            }
            long size = 0;
            List<Path> files = new ArrayList<>();
            try (Stream<Path> walk = Files.walk(pathToFiles)) {
                size = walk.count();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (Stream<Path> walk = Files.walk(pathToFiles)) {
                files = walk.filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(fileName -> fileName.endsWith(".txt"))
                        .limit(numberOfFiles)
                        .map(Paths::get)
                        .toList();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Pattern emailPattern = Pattern.compile("\\b(.+)@(\\S+)\\b");
            final Pattern phoneNumberPattern = Pattern.compile("\\+\\(\\d{2}\\)\\d{7}\\b");
            final Pattern docNumberPattern = Pattern.compile("\\b\\d{4}-[a-zа-я]{3}-\\d{4}-[a-zа-я]{3}-\\d[a-zа-я]\\d[a-zа-я]\\b");

            if (files.size() > 0) {
                for (Path aPath : files) {
                    String phoneNumber = "Телефонный номер не определен";
                    String email = "email не определен";
                    List<String> docNumbers = new ArrayList<>();
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(aPath.toFile()))) {

                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            Matcher emailMatcher = emailPattern.matcher(line);
                            Matcher phoneNumberMatcher = phoneNumberPattern.matcher(line);
                            Matcher docNumberMatcher = docNumberPattern.matcher(line);

                            if (emailMatcher.find()) {
                                email = emailMatcher.group();
                            }
                            if (phoneNumberMatcher.find()) {
                                phoneNumber = phoneNumberMatcher.group();
                            }
                            if (docNumberMatcher.find()) {
                                docNumbers.add(docNumberMatcher.group());
                            }
                        }

                        Document document = new Document(docNumbers, phoneNumber, email);
                        String fileName = aPath.getFileName().toString();

                        map.put(fileName, document);

                        System.out.println("Всего было обработано " + size + " документов. Из них невалидного формата - " +
                                (size - files.size()) + " штук");

                    } catch (IOException e) {
                        System.out.println("Ошибка в чтении файла");
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (var element : map.entrySet()) {
            System.out.println(element.getKey() + " " + element.getValue());
        }
    }
}