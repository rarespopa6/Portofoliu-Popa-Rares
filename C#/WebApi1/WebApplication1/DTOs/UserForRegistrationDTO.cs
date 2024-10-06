namespace WebApplication1.DTOs;

public class UserForRegistrationDTO
{
    public string Email { get; set; }
    public string Password { get; set; }
    public string PasswordConfirm { get; set; }

    public UserForRegistrationDTO(string email, string password, string passwordConfirm)
    {
        Email ??= "";
        Password ??= "";
        PasswordConfirm ??= "";
    }

}