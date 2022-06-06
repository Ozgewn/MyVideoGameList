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
        /// <summary>
        /// <b>Cabecera:</b> public bool isNombreUsuarioExistenteBL(String nombreUsuario) <br />
        /// <b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta compruebe si existe un usuario con exactamente el
        /// mismo nombre de usuario que el nombreUsuario introducido por parámetros <br />
        /// <b>Precondiciones:</b> N/A <br />
        /// <b>Postcondiciones:</b> Se devolverá un booleano: true si existe, false si no existe <br />
        /// <b>Entrada:</b> String nombreUsuario. El nombre de usuario del cual se desea comprobar la existencia en la BBDD <br />
        /// <b>Salida:</b> bool nombreUsuarioExiste. true si existe, false si no existe <br />
        /// </summary>
        /// <param name="nombreUsuario"><b>nombreUsuario - String. </b>El nombre de usuario que se desea buscar en la BBDD para comprobar si existe o no</param>
        /// <returns>nombreUsuarioExiste - bool. true si existe el usuario con dicho nombre de usuario, false si no existe el usuario en cuestion</returns>
        public bool isNombreUsuarioExistenteBL(String nombreUsuario)
        {
            return new clsListadoUsuariosDAL().isNombreUsuarioExistenteDAL(nombreUsuario);
        }
        /// <summary>
        /// <b>Cabecera:</b> public List<clsUsuario> getListadoCompletoUsuariosDAL() <br />
        /// <b>Descripción:</b> Este método se encarga de devolver una lista de usuarios completa<br />
        /// <b>Precondiciones:</b>Se debe tener conexión a Internet, debe existir 1 o más usuarios en la BBDD<br />
        /// <b>Postcondiciones:</b> Se devolvera un listado completo con todos los usuarios encontrados en la BBDD <br />
        /// <b>Salida:</b> List<clsUsuario> oListadoCompletoUsuarios. Un listado completo de usuarios<br />
        /// </summary>
        /// <returns><b>oListadoCompletoUsuarios - List<clsUsuario>. </b>Un listado completo de usuarios</returns>
        public List<clsUsuario> getListadoCompletoUsuariosBL()
        {
            return new clsListadoUsuariosDAL().getListadoCompletoUsuariosDAL();
        }
    }
}
