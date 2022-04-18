using Microsoft.AspNetCore.Mvc;
using MVGL_API.Models;
using MVGL_BL.Gestora;
using MVGL_BL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ListaVideojuegosController : ControllerBase
    {
        // GET api/<ListaVideojuegosController>/5
        [HttpGet("{id}")]
        /*
         * Este get nos devolvera una lista completa de videojuegos con su informacion correspondiente en la lista (es decir, nos traera la nota del usuario, la nota media), esto quiere
         * decir que tendremos que "unir" 2 clases en una, ya que este get nos dara informacion de la lista del usuario e informacion en si del propio juego.
         * Diferenciaremos si un juego esta en la lista del usuario o no gracias al idUsuario, si el idUsuario es null significa que no esta en la lista del usuario.
         * Este get lo hacemos para que en la APP podamos ver un listado completo de videojuegos, mostrandonos la informacion del juego y la informacion de nuestra lista, por ejemplo:
         * Nombre: The witcher 3, Nota (esto seria la nota del usuario): 8, Nota media: 9'1
         * ... y asi con todos los juegos esten o no en la lista del usuario
         */
        public ObjectResult Get(string id)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = null;
            try
            {
                List<clsVideojuego> listaVideojuegosCompleto = new clsListadoVideojuegosBL().getListadoVideojuegosCompletoBL();
                List<clsListaVideojuego> listadoDeListaVideojuegos = new clsListadoListaVideojuegosBL().getListaVideojuegosDeUsuarioBL(id);
                List<clsListaConInfoDeVideojuego> listadoDeVideojuegosConInfo = new List<clsListaConInfoDeVideojuego>();

                List<clsVideojuego> listaVideojuegosQueEstanEnLista = new List<clsVideojuego>();
                List<clsVideojuego> listaVideojuegosQueNoEstanEnLista = new List<clsVideojuego>();
                foreach (clsListaVideojuego oLista in listadoDeListaVideojuegos)
                {
                    listaVideojuegosQueEstanEnLista.Add((from videojuego in listaVideojuegosCompleto
                                                         where videojuego.Id == oLista.IdVideojuego && oLista.IdUsuario.Equals(id)
                                                         select videojuego).FirstOrDefault()); //Este linq va añadiendo a la lista los videojuegos que esten en la lista

                    listadoDeVideojuegosConInfo.Add(new clsListaConInfoDeVideojuego(oLista, (from videojuego in listaVideojuegosCompleto
                                                                                             where videojuego.Id == oLista.IdVideojuego && oLista.IdUsuario.Equals(id)
                                                                                             select videojuego).FirstOrDefault())); //Este linq va añadiendo a la lista los videojuegos que esten en la lista de videojuegos del usuario y la info de los propios juegos
                }
                listaVideojuegosQueNoEstanEnLista.AddRange(listaVideojuegosCompleto);
                listaVideojuegosQueNoEstanEnLista = listaVideojuegosQueNoEstanEnLista.Except(listaVideojuegosQueEstanEnLista).ToList();
                foreach (clsVideojuego oVideojuego in listaVideojuegosQueNoEstanEnLista)
                {
                    listadoDeVideojuegosConInfo.Add(new clsListaConInfoDeVideojuego(oVideojuego)); //añado los videojuegos sin su info, esto significa que no estan en la lista del usuario
                }
                result.Value = listadoDeVideojuegosConInfo;
                result.StatusCode = (int)HttpStatusCode.OK;
                if((result.Value as List<clsListaConInfoDeVideojuego>) == null || (result.Value as List<clsListaConInfoDeVideojuego>).Count == 0)
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
        // POST api/<ListaVideojuegosController>
        [HttpPost]
        public ObjectResult Post([FromBody] clsListaVideojuego value)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraListaVideojuegosBL().insertarVideojuegoEnListaBL(value);
                if (!result.Value.ToString().Equals("4"))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //no controlamos que sea mas de 1 ya que no podra insertar mas de 1 fila con la instruccion
                }
                else //en cambio, si inserta 4 fila significa que todo ha ido bien
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
        // PUT api/<ListaVideojuegosController>
        [HttpPut]
        public ObjectResult Put([FromBody] clsListaVideojuego value)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraListaVideojuegosBL().editarVideojuegoDeListaBL(value);
                if (!result.Value.ToString().Equals("4"))
                {
                    result.StatusCode = (int)HttpStatusCode.NotFound; //no controlamos que sea mas de 1 ya que no podra insertar mas de 1 fila con la instruccion
                }
                else //en cambio, si edita 4 fila significa que todo ha ido bien
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
        // DELETE api/<ListaVideojuegosController>/idUsuario/5/idVideojuego/5
        [HttpDelete("Usuario/{idUsuario}/Videojuego/{idVideojuego}")]
        /*
         * Este metodo intentara borrar a un usuario, al intentar borrarlo, en la BBDD se desencadenara un trigger el cual,
         * primero borrara todos los juegos en la lista del usuario, actualizara la nota media y dificultad media de los juegos que
         * se han visto involucrados en el borrado de los juegos de la lista del usuario, y acto seguido, borrara al usuario propiamente dicho
         */
        public ObjectResult Delete(string idUsuario, int idVideojuego)
        {
            ObjectResult result = new ObjectResult(new { });
            result.Value = 0;

            try
            {
                result.Value = new clsGestoraListaVideojuegosBL().borrarVideojuegoDeListaBL(idUsuario, idVideojuego);
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
