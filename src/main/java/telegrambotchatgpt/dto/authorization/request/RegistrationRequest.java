package telegrambotchatgpt.dto.authorization.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    @NotBlank(message = "Username should be valid")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirmation is required")
    private String confirmPassword;

    @AssertTrue(message = "Password must be at least 8 characters long, less than 50 characters "
            + "and contain at least one letter and one digit, and match the confirmation.")
    public boolean isPasswordValid() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,49}") && password.equals(confirmPassword);
    }
}
