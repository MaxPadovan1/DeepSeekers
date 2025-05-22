import com.example.teach.model.AIService;

public class AITest {

    public static void main(String[] args) {
        String prompt = "Explain the concept of polymorphism in Java.";

        System.out.println("[TEST] Sending prompt to AI...");
        String response = AIService.getInstance().getResponse(prompt);

        System.out.println("\n=== AI Response ===");
        System.out.println(response);
    }
}

