import com.example.teach.model.AIService;

public class AITest {

    public static void main(String[] args) {
        String prompt = "In 2–3 concise paragraphs, explain polymorphism in Java using a real-world metaphor. Include just one code example.";


        System.out.println("[TEST] Sending prompt to AI...");
        String response = AIService.getInstance().getResponse(prompt);

        System.out.println("\n=== AI Response ===");
        System.out.println(response);
    }
}
