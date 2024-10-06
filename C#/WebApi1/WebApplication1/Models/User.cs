namespace WebApplication1.Models;

public partial class User
{
    public int UserId { get; set; }
    public string  FirstName { get; set; }
    public string LastName { get; set; }
    public string Email { get; set; }
    public string Gender { get; set; }
    public bool Active { get; set; }

    public User(int UserId, string FirstName, string LastName, string Email, string Gender, bool Active)
    {
        this.UserId = UserId;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Gender = Gender;
        this.Active = Active;
    }
    
}