using MVGL_DAL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Listados
{
    public class clsListadoListaVideojuegosBL
    {
        /// <summary>
        /// <b>Cabecera:</b> public List<clsListaVideojuego> getListaVideojuegosDeUsuarioBL(String idUsuario)<br />
        /// <b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta devuelva la lista de videojuegos del usuario introducido por parametros<br />
        /// <b>Precondiciones:</b> El usuario debe tener al menos 1 juego en su lista, el idusuario debe ser válido<br />
        /// <b>Postcondiciones:</b> Se devolverá la lista de videojuegos del usuario introducido por parametros<br />
        /// <b>Entrada:</b> String idUsuario. El id del usuario del cual se desea obtener su lista de videojuegos<br />
        /// <b>Salida:</b>  List<clsListaVideojuegos> listadoVideojuegosDeUsuario. El listado de videojuegos del usuario<br />
        /// </summary>
        /// <param name="idUsuario"><b>idUsuario - int. <b/>El id del usuario del cual se desea obtener la lista de videojuegos</param>
        /// <returns><b>listadoVideojuegosDeUsuario - List<clsListaVideojuego>.</b> La lista de videojuegos completa traida de la BBDD que corresponde al id del usuario introducido por parametros</returns>
        public List<clsListaVideojuego> getListaVideojuegosDeUsuarioBL(String idUsuario)
        {
            return new clsListadoListaVideojuegosDAL().getListaVideojuegosDeUsuarioDAL(idUsuario);
        }
    }
}
