using Microsoft.AspNetCore.Mvc;
using MVGL_BL.Gestora;
using MVGL_BL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Net;

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuarioController : ControllerBase
    {
        // GET api/<UsuarioController>/5
        [HttpGet("{id}")]
        public ObjectResult Get(string id)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = null;
            try
            {
                result.Value = new clsListadoUsuariosBL().getUsuarioSegunIdBL(id);
                if ((result.Value as clsUsuario) == null || (result.Value as clsUsuario).Id.Equals("")) //si tiene el id vacio significa que no ha encontrado al usuario especificado en la BBDD
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
        // POST api/<UsuarioController>
        [HttpPost]
        public ObjectResult Post([FromBody] clsUsuario value)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraUsuariosBL().insertarUsuarioBL(value);
                if (!result.Value.ToString().Equals("1"))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //no controlamos que sea mas de 1 ya que no podra insertar mas de 1 fila con la instruccion
                }
                else //en cambio, si inserta 1 fila significa que todo ha ido bien
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

        // PUT api/<UsuarioController>
        [HttpPut]
        public ObjectResult Put([FromBody] clsUsuario value)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraUsuariosBL().editarUsuarioBL(value);
                if (!result.Value.ToString().Equals("1"))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //no controlamos que sea mas de 1 ya que no podra insertar mas de 1 fila con la instruccion
                }
                else //en cambio, si edita 1 fila significa que todo ha ido bien
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

        // DELETE api/<UsuarioController>/5
        [HttpDelete("{id}")]
        public ObjectResult Delete(string id)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraUsuariosBL().borrarUsuarioBL(id);
                if (!result.Value.ToString().Equals("1") || !result.Value.ToString().Equals("0"))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //esta instruccion deberia modificar mas 1 fila
                }
                else //si modifica mas de 1 fila, es que parece que todo ha ido correcto, ya que no se sabra cuantas filas necesita modificar,
                //ya que este metodo borrara al usuario de la BBDD lo que desencadenara un trigger, el cual, borrara a su vez todos los juegos que esten
                //en la lista del usuario a borrar, por lo cual, no sabremos cuantas filas va a borrar, solo sabemos que borrara mas de 1
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
    }
}
