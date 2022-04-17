using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsListaVideojuego
    {
        private int idVideojuego, nota, dificultad;
        private DateTime? fechaDeComienzo, fechaDeFinalizacion;
        private int estado;
        private String idUsuario;

        public clsListaVideojuego()
        {
            this.idUsuario = null;
            this.idVideojuego = 0;
            fechaDeComienzo = null;
            fechaDeFinalizacion = null;
        }

        public clsListaVideojuego(String idUsuario, int idVideojuego, DateTime? fechaDeComienzo, DateTime? fechaDeFinalizacion, int nota, int dificultad, int estado)
        {
            this.idUsuario = idUsuario;
            this.idVideojuego = idVideojuego;
            this.fechaDeComienzo = fechaDeComienzo;
            this.fechaDeFinalizacion = fechaDeFinalizacion;
            this.nota = nota;
            this.dificultad = dificultad;
            this.estado = estado;
        }

        public String IdUsuario { get => idUsuario; set => idUsuario = value; }
        public int IdVideojuego { get => idVideojuego; set => idVideojuego = value; }
        public DateTime? FechaDeComienzo { get => fechaDeComienzo; set => fechaDeComienzo = value; }
        public DateTime? FechaDeFinalizacion { get => fechaDeFinalizacion; set => fechaDeFinalizacion = value; }
        public int Nota { get => nota; set => nota = value; }
        public int Dificultad { get => dificultad; set => dificultad = value; }
        public int Estado { get => estado; set => estado = value; }
    }
}
