using MVGL_DAL.Listados;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Listados
{
    public class clsListadoUsuariosBL
    {
        /// <summary>
        /// <b>Cabecera:</b> public clsUsuario getUsuarioSegunIdBL(int id)<br />
        /// <b>Descripción:</b> Este método se encarga de llamar a la capa DAL y que esta obtenga toda la información del usuario de la BBDD que corresponda con el id pasado por parámetros<br />
        /// <b>Precondiciones:</b> El usuario debe existir, el id del usuario debe ser válido<br />
        /// <b>Postcondiciones:</b> Se devolverá la información relevante del usuario que corresponda con el id introducido por parámetros<br />
        /// <b>Entrada:</b> String id. El id del usuario del cual se desea obtener la información<br />
        /// <b>Salida:</b> clsUsuario oUsuario. El objeto usuario con toda su respectiva información obtenida de la BBDD<br />
        /// </summary>
        /// <param name="id"><b>id - String</b>. El id del usuario del cuál se desea obtener la información </param>
        /// <returns><b>oUsuario - clsUsuario.</b> El usuario con toda la información</returns>
        public clsUsuario getUsuarioSegunIdBL(String id)
        {
            return new clsListadoUsuariosDAL().getUsuarioSegunIdDAL(id);
        }
    }
}
