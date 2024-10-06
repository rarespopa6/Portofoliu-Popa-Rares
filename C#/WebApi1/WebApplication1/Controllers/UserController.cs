using Microsoft.AspNetCore.Mvc;
using WebApplication1.Data;
using WebApplication1.DTOs;
using WebApplication1.Models;

namespace WebApplication1.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    private DataContextDapper _dapper;
    
    public UserController(IConfiguration config)
    {
        _dapper = new DataContextDapper(config);
    }

    [HttpGet("test", Name = "TestConnection1")]
    public DateTime TestConnection()
    {
        return _dapper.LoadDataSingle<DateTime>("SELECT GETDATE()");
    }
    
    [HttpGet("first", Name = "GetFirst1")]
    public string GetFirstUserName(string userName)
    {
        return "Hello " + userName;
    }
    
    [HttpGet("second/{userName}", Name = "GetSecond1")]
    public string GetSecondUserName(string userName)
    {
        return "Hello " + userName;
    }

    [HttpGet("all", Name = "1")]
    public IEnumerable<User> GetUsers()
    {
        return _dapper.LoadData<User>("SELECT * FROM User");
    }

    [HttpGet("user", Name = "GetUser1")]
    public User GetUser(int userId)
    {
        return _dapper.LoadDataSingle<User>($"SELECT * FROM User WHERE UserId = {userId}");
    }

    [HttpPost("user", Name = "PostUser1")]
    public IActionResult PostUser(UserDTO user)
    {
        // if ...
        // _dapper.AddUser(user)
        return Ok("User Created");
        // else throw Exception
    }

    [HttpPut("user", Name = "PutUser1")]
    public IActionResult PutUser(UserDTO user)
    {
        // _dapper.UpdateUser(user)
        return Ok("User Updated");
    }

    [HttpDelete("user", Name = "DeleteUser1")]
    public IActionResult DeleteUser(int userId)
    {
        // _dapper.DeleteUser(userId)
        return Ok("User Deleted");
    }
}