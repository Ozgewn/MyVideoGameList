using MVGL_DAL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Listados
{
    public class clsListadoEstadosBL
    {
        /// <summary>
        /// <b>Cabecera:</b> public List<clsEstado> getListadoCompletoEstadosBL() <br />
        /// <b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta traiga una lista completa de todos los posibles estados
        /// de un videojuego dentro de una lista de usuario<br />
        /// <b>Precondiciones:</b> N/A <br />
        /// <b>Postcondiciones:</b> Se devolvera una lista con el id y el nombre de cada estado <br />
        /// <b>Entrada:</b> N/A <br />
        /// <b>Salida:</b> List<clsEstado> listadoCompletoEstados. Un listado completo de todos los posibles estados
        /// de un videojuego dentro de una lista de usuario, traido desde la BBDD<br />
        /// </summary>
        /// <returns><b>listadoCompletoEstados - List<clsEstado>. </b> Listado completo de los posibles estados de un videojuego</returns>
        public List<clsEstado> getListadoCompletoEstadosBL()
        {
            return new clsListadoEstadosDAL().getListadoCompletoEstadosDAL();
        }
    }
}
