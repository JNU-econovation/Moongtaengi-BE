package econovation.moongtaengi.admin.api.dto;

public record AdminLoginResponse(
        String accessToken,
        String nickname,
        String role
) {}
