using Microsoft.AspNetCore.Mvc;
using MVGL_API.Models;
using MVGL_BL.Listados;
using MVGL_Entidades;
using System.Collections.Generic;
using System.Linq;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace MVGL_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ListaVideojuegosController : ControllerBase
    {
        // GET api/<ListaVideojuegosController>/5
        [HttpGet("{id}")]
        public List<clsListaConInfoDeVideojuego> Get(string id)
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
            foreach(clsVideojuego oVideojuego in listaVideojuegosQueNoEstanEnLista)
            {
                listadoDeVideojuegosConInfo.Add(new clsListaConInfoDeVideojuego(oVideojuego)); //añado los videojuegos sin su info, esto significa que no estan en la lista del usuario
            }
            return listadoDeVideojuegosConInfo;
        }
    }
}
