using Microsoft.AspNetCore.Mvc;
using MVGL_BL.Listados;
using MVGL_Entidades;
using System;
using System.Net;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class VideojuegoController : ControllerBase
    {
        // GET api/<VideojuegoController>/5
        [HttpGet("{id}")]
        public ObjectResult Get(int id)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = null;
            try
            {
                result.Value = new clsListadoVideojuegosBL().getVideojuegoSegunIdBL(id);
                result.StatusCode = (int)HttpStatusCode.OK;
                if ((result.Value as clsVideojuego) == null || (result.Value as clsVideojuego).Id == 0)
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound;
                }
            }
            catch (Exception)
            {
                result.StatusCode = (int)HttpStatusCode.InternalServerError;
            }
            return result;
        }
    }
}
