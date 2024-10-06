using WebApplication1.Models;

namespace WebApplication1.Data;

public interface IUserRepository
{
    public bool SaveChanges();
    public void AddEntity<T>(T entity);
    public void RemoveEntity<T>(T entity);
    public IEnumerable<User> GetUsers();
    public User GetUser(int userId);
}