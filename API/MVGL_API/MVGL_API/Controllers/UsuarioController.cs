using Microsoft.AspNetCore.Mvc;
using MVGL_BL.Gestora;
using System;
using System.Net;

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuarioController : ControllerBase
    {
        // POST api/<UsuarioController>/342562362
        [HttpPost]
        public ObjectResult Post(string id)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraUsuariosBL().insertarUsuarioBL(id);
                if (!result.Value.ToString().Equals(1))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //no controlamos que sea mas de 1 ya que no podra insertar mas de 1 fila con la instruccion
                }
                else //si por ejemplo, inserta 0 filas, eso significara un error
                {
                    result.StatusCode = (int)HttpStatusCode.OK;
                }
            }
            catch (Exception)
            {
                result.StatusCode = (int)HttpStatusCode.InternalServerError;
            }

            return result;
        }

        // PUT api/<UsuarioController>/5
        [HttpPut("{id}")]
        public void Put(string id, [FromBody] string value)
        {
            //TODO: Editar info usuario
        }

        // DELETE api/<UsuarioController>/5
        [HttpDelete("{id}")]
        public void Delete(string id)
        {
            //TODO: Borrar usuario
        }
    }
}
