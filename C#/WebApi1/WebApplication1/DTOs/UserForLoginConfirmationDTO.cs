namespace WebApplication1.DTOs;

public class UserForLoginConfirmationDTO
{
    public byte[] PasswordHash { get; set; }
    public byte[] PasswordSalt { get; set; }

    UserForLoginConfirmationDTO(byte[] passwordHash, byte[] passwordSalt)
    {
        PasswordHash ??= new byte[0];
        PasswordSalt ??= new byte[0];
    }
}