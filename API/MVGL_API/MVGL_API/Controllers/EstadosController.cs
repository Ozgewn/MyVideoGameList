using Microsoft.AspNetCore.Mvc;
using MVGL_BL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Net;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class EstadosController : ControllerBase
    {
        // GET: api/<EstadosController>
        [HttpGet]
        public ObjectResult Get()
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = null;
            try
            {
                result.Value = new clsListadoEstadosBL().getListadoCompletoEstadosBL();
                result.StatusCode = (int)HttpStatusCode.OK;
                if ((result.Value as List<clsEstado>) == null || (result.Value as List<clsEstado>).Count == 0)
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
