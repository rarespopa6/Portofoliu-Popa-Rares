namespace WebApplication1.DTOs;

public class UserForLoginDTO
{
    public string Email { get; set; }
    public string Password { get; set; }

    public UserForLoginDTO(string email, string password)
    {
        Email ??= "";
        Password ??= "";
    }
}