using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.Data;
using WebApplication1.DTOs;
using WebApplication1.Models;

namespace WebApplication1.Controllers;

[ApiController]
[Route("[controller]")]
public class UserEfController : ControllerBase
{
    private DataContextEF _dataContext;
    private IUserRepository _userRepository;
    
    public UserEfController(IConfiguration config, IUserRepository userRepository)
    {
        _dataContext = new DataContextEF(config);
        _userRepository = userRepository;
    }

    [HttpGet("test", Name = "TestConnection")]
    public DateTime TestConnection()
    {
        return DateTime.Now;
    }
    
    [HttpGet("first", Name = "GetFirst")]
    public string GetFirstUserName(string userName)
    {
        return "Hello " + userName;
    }
    
    [HttpGet("second/{userName}", Name = "GetSecond")]
    public string GetSecondUserName(string userName)
    {
        return "Hello " + userName;
    }

    [HttpGet("all", Name = "GetUsers")]
    public IEnumerable<User> GetUsers()
    {
        return _userRepository.GetUsers();
    }

    [HttpGet("user", Name = "GetUser")]
    public User GetUser(int userId)
    {
        return _userRepository.GetUser(userId);
        // TODO sau: _dataContext.Users.Where(u => u.UserId == userId);
    }

    [HttpPost("user", Name = "AddUser")]
    public IActionResult AddUser(User user)
    {
        _userRepository.AddEntity(user);
        // User userEntity = _mapper.Map<User>(user);
        if (_userRepository.SaveChanges())
        {
            return Ok("User created");
        }
        return BadRequest("User not created");
        // if ...
        // _dapper.AddUser(user)
        // else throw Exception
    }

    [HttpPut("user", Name = "PutUser")]
    public IActionResult PutUser(UserDTO user)
    {
        // _dapper.UpdateUser(user)
        return Ok("User Updated");
    }

    [HttpDelete("user", Name = "DeleteUser")]
    public IActionResult DeleteUser(int userId)
    {
        // _dapper.DeleteUser(userId)
        return Ok("User Deleted");
    }
}