using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Microsoft.VisualBasic.CompilerServices;
using WebApplication1.Data;
using WebApplication1.DTOs;
using WebApplication1.Service;

namespace WebApplication1.Controllers;

[Authorize]
[ApiController]
[Route("[controller]")]
public class AuthController : ControllerBase
{
    private readonly DataContextDapper _dapper;
    private AuthService _authService;
    
    public AuthController(IConfiguration config)
    {
        _dapper = new DataContextDapper(config);
        _authService = new AuthService(config);
    }
    
    [AllowAnonymous]
    [HttpPost("register")]
    public IActionResult Register(UserForRegistrationDTO user)
    {
        if (user.Password != user.PasswordConfirm)
            return BadRequest("Passwords do not match");
        // logic
        return Ok();
    }
    
    [AllowAnonymous]
    [HttpPost("login")]
    public IActionResult Login(UserForLoginDTO user)
    {
        string sqlForHashAndSalt = @"SELECT 
                [PasswordHash],
                [PasswordSalt] FROM TutorialAppSchema.Auth WHERE Email = '" +
                                   user.Email + "'";

        UserForLoginConfirmationDTO userForConfirmation = _dapper
            .LoadDataSingle<UserForLoginConfirmationDTO>(sqlForHashAndSalt);

        byte[] passwordHash = _authService.GetPasswordHash(user.Password, userForConfirmation.PasswordSalt);
        for (int index = 0; index < passwordHash.Length; index++)
        {
            if (passwordHash[index] != userForConfirmation.PasswordHash[index]){
                return StatusCode(401, "Incorrect password!");
            }
        }
        
        string userIdSql = @"
                SELECT UserId FROM TutorialAppSchema.Users WHERE Email = '" +
                           user.Email + "'";

        int userId = _dapper.LoadDataSingle<int>(userIdSql);
        
        return Ok(new Dictionary<string, string> {
            {"token", _authService.CreateToken(userId)}
        });
    }

    [HttpGet("refresh-token")]
    public string RefreshToken()
    {
        // string userId = User.FindFirst("userId");
        // TEST
        int userId = 1;
        return _authService.CreateToken(userId);
    }
}