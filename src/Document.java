import java.util.List;

public class Document {

    private List<String> documentNumber;
    private String phoneNumber;
    private String email;

    public Document(List<String> documentNumber, String phoneNumber, String email) {
        this.documentNumber = documentNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (documentNumber.size() > 0) {
            result.append("Номера документов: ");
            for (String elem : documentNumber) {
                result.append(elem).append("; ");
            }
        }
        result.append("Телефонный номер: ").append(phoneNumber)
                .append(" email: ").append(email);

        return result.toString();
    }
}
