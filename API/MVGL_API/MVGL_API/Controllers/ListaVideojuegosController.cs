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
        // GET: api/<ListaVideojuegosController>
        [HttpGet]
        public List<clsListaVideojuego> Get()
        {
            List<clsVideojuego> listaVideojuegosCompleto = new clsListadoVideojuegosBL().getListadoVideojuegosCompletoBL();
            List<clsListaVideojuego> listadoDeListaVideojuegos = new clsListadoListaVideojuegosBL().getListaVideojuegosDeUsuarioBL("aa");
            List<clsListaConInfoDeVideojuego> listadoDeVideojuegosConInfo = new List<clsListaConInfoDeVideojuego>();

            List<clsVideojuego> listaVideojuegosQueEstanEnLista = new List<clsVideojuego>();
            List<clsVideojuego> listaVideojuegosQueNoEstanEnLista = new List<clsVideojuego>();
            foreach (clsListaVideojuego oLista in listadoDeListaVideojuegos)
            {
                listaVideojuegosQueEstanEnLista.AddRange(new List<clsVideojuego>(from videojuego in listaVideojuegosCompleto
                                                                                              where videojuego.Id == oLista.IdVideojuego
                                                                                              select videojuego));
            }
            listaVideojuegosQueNoEstanEnLista.AddRange(listaVideojuegosCompleto);
            listaVideojuegosQueNoEstanEnLista = listaVideojuegosQueNoEstanEnLista.Except(listaVideojuegosQueEstanEnLista).ToList();
            return new clsListadoListaVideojuegosBL().getListaVideojuegosDeUsuarioBL("aa");
        }

        // GET api/<ListaVideojuegosController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<ListaVideojuegosController>
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

        // PUT api/<ListaVideojuegosController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<ListaVideojuegosController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
