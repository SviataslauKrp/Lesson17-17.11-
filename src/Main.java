import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                     "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                     "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                     "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}