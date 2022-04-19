using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsEstado
    {
        private int id;
        private String nombreEstado;

        public clsEstado()
        {
            id = 0;
            nombreEstado = null;
        }
        public clsEstado(int id, String nombreEstado)
        {
            this.id = id;
            this.nombreEstado=nombreEstado;
        }
        
        public int Id { get => id; set => id = value; }
        public String NombreEstado { get => nombreEstado; set => nombreEstado = value;}
    }
}
