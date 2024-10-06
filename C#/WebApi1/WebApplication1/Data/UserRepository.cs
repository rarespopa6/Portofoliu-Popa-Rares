using WebApplication1.Models;

namespace WebApplication1.Data;

public class UserRepository : IUserRepository
{
    private DataContextEF _dataContext;

    public UserRepository(IConfiguration config)
    {
        _dataContext = new DataContextEF(config);
    }

    public bool SaveChanges()
    {
        return _dataContext.SaveChanges() > 0;
    }

    public void AddEntity<T>(T entity)
    {
        if (entity == null) throw new ArgumentNullException(nameof(entity));
        _dataContext.Add(entity);
    }
    
    public void RemoveEntity<T>(T entity)
    {
        if (entity == null) throw new ArgumentNullException(nameof(entity));
        _dataContext.Add(entity);
    }
    
    public IEnumerable<User> GetUsers()
    {
        return _dataContext.Users.ToList();
    }
    
    public User GetUser(int userId)
    {
        User user = _dataContext.Users.Find(userId);
        return (user != null) ? user : throw new KeyNotFoundException();
        // TODO sau: _dataContext.Users.Where(u => u.UserId == userId);
    }
}