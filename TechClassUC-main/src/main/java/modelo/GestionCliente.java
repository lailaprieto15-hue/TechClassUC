package modelo;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCliente {

    private final ArrayDeque<Cliente> colaClientes;

    public GestionCliente() {
        colaClientes = new ArrayDeque<>();
    }

    public void agregarCliente(Cliente cliente) {
        colaClientes.add(cliente);
    }

    public void agregarClienteAlFrente(Cliente cliente) {
        colaClientes.addFirst(cliente);
    }

    public Cliente eliminarCliente(int id) {
        Cliente eliminado = null;
        for (Cliente c : colaClientes) {
            if (c.getId() == id) {
                eliminado = c;
                break;
            }
        }
        if (eliminado != null) {
            colaClientes.remove(eliminado);
        }
        return eliminado;
    }

    public Cliente buscarClienteEnEspera(int id) {
        for (Cliente c : colaClientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Cliente atenderClientePorPrioridad(String prioridad) {
        Cliente cliente = null;
        for (Cliente c : colaClientes) {
            if (c.getPrioridad().equalsIgnoreCase(prioridad)) {
                cliente = c;
                break;
            }
        }
        if (cliente != null) {
            colaClientes.remove(cliente);
        }
        return cliente;
    }

    public Cliente clienteActual() {
        return colaClientes.peek();
    }

    public Cliente atenderCliente() {
        return colaClientes.poll();
    }

    public List<Cliente> getClientesEnEspera() {
        return colaClientes.stream().collect(Collectors.toList());
    }

    public int totalClientesEnEspera() {
        return colaClientes.size();
    }

    public boolean colaVacia() {
        return colaClientes.isEmpty();
    }
}


