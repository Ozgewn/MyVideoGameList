using MVGL_DAL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Listados
{
    public class clsListadoVideojuegosBL
    {
        /// <summary>
        ///<b>Cabecera:</b> public clsVideojuego getVideojuegoSegunIdBL(String id)<br />
        ///<b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta devuelva la información correspondiente al videojuego introducido por parametros<br />
        ///<b> Precondiciones:</b> El id del videojuego debe corresponder a un videojuego existente<br />
        ///<b> Postcondiciones:</b> Se devolvera la informacion del videojuego en cuestion<br />
        ///<b>Entrada:</b> String id. El id del videojuego del cual se desea obtener la informacion<br />
        ///<b>Salida:</b> clsVideojuego oVideojuego. El videojuego con toda su respectiva informacion<br />
        /// </summary>
        /// <param name="id"><b>id - String. </b>El id del videojuego del cual se desea obtener la informacion de la BBDD</param>
        /// <returns><b>oVideojuego - clsVideojuego. </b>El videojuego traido de la BBDD que corresponde con el id que se ha introducido por parametros</returns>
        public clsVideojuego getVideojuegoSegunIdBL(int id)
        {
            return new clsListadoVideojuegosDAL().getVideojuegoSegunIdDAL(id);
        }
        /// <summary>
        ///<b>Cabecera:</b> public List<clsVideojuego> getListadoVideojuegosCompletoBL() <br />
        ///<b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta obtenga todos los videojuegos de la BBDD <br />
        ///<b> Precondiciones:</b> Deben haber videojuegos en la BBDD <br />
        ///<b> Postcondiciones:</b> Se devolvera la lista completa de los videojuegos en la BBDD <br />
        ///<b>Entrada:</b> N/A <br />
        ///<b>Salida:</b> List<clsVideojuego> listadoCompletoVideojuegos. El listado completo de videojuegos <br />
        /// </summary>
        /// <returns><b>listadoCompletoVideojuegos - List<clsVideojuego>. </b>El listado completo de videojuegos traido de la BBDD</returns>
        public List<clsVideojuego> getListadoVideojuegosCompletoBL()
        {
            return new clsListadoVideojuegosDAL().getListadoVideojuegosCompletoDAL();
        }
    }
}
