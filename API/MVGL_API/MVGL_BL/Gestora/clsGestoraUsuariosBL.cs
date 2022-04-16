using MVGL_DAL.Gestora;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Gestora
{
    public class clsGestoraUsuariosBL
    {
        /// <summary>
        /// <b>Cabecera:</b>public int insertarUsuarioBL(String id)<br />
        /// <b>Descripción:</b>Este método se encarga de llamar a la capa DAL y que esta inserte un usuario con el id correspondiente a la BBDD, con todos los datos con valores por defecto (0 todos)<br />
        /// <b>Precondiciones:</b>El id debe ser valido, y no exceder los 28 caracteres<br />
        /// <b>Postcondiciones:</b>El usuario se creera en la BBDD con su correspondiente id<br />
        /// <b>Entrada:</b>String id. El id del usuario a crear<br />
        /// <b>Salida:</b>int resultado. El número de filas afectadas, si es 1 significa que se ha insertado exitosamente, si es distinto de 1, habrá ocurrido algún fallo<br />
        /// </summary>
        /// <param name="id"><b>id - String. El id del usuario a crear en la BBDD</b> </param>
        /// <returns><b>resultado - int.El número de filas afectadas por la instrucción de inserción en la BBDD</b> </returns>
        public int insertarUsuarioBL(String id)
        {
            return new clsGestoraUsuariosDAL().insertarUsuarioDAL(id);
        }
    }
}
