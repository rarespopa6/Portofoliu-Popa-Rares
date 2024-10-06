namespace WebApplication1.DTOs;

public class UserDTO
{
    public string  FirstName { get; set; }
    public string LastName { get; set; }
    public string Email { get; set; }
    public string Gender { get; set; }
    public bool Active { get; set; }

    public UserDTO(string FirstName, string LastName, string Email, string Gender, bool Active)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Gender = Gender;
        this.Active = Active;
    }
}