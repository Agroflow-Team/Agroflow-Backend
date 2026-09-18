import com.fasterxml.jackson.databind.ObjectMapper;
public class TestJson {
    public static void main(String[] args) throws Exception {
        String json = "{\"private_key\": \"hello\\nworld\"}";
        String replaced = json.replace("\\n", "\n");
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(replaced);
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.err.println("FAILED: " + e.getMessage());
        }
    }
}
