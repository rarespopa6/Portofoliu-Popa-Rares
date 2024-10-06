using System.Data;
using Dapper;
using Microsoft.Data.SqlClient;

namespace WebApplication1.Data;

public class DataContextDapper
{
    private readonly IConfiguration _configuration;

    public DataContextDapper(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    public IEnumerable<T> LoadData<T>(string sql)
    {
        IDbConnection dbConnection = new SqlConnection(_configuration.GetConnectionString("DefaultConnection"));
        return dbConnection.Query<T>(sql);
    }
    
    public T LoadDataSingle<T>(string sql)
    {
        IDbConnection dbConnection = new SqlConnection(_configuration.GetConnectionString("DefaultConnection"));
        return dbConnection.QuerySingle<T>(sql);
    }

    public bool Execute(string sql)
    {
        IDbConnection dbConnection = new SqlConnection(_configuration.GetConnectionString("DefaultConnection"));
        return dbConnection.Execute(sql) > 0;
    }
}