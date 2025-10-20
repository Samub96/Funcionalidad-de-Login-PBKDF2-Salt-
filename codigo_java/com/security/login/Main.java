package com.security.login;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initDatabase();
        UserService service = new UserService();
        Scanner sc = new Scanner(System.in);

        // Configuración inicial: crear admin
        if (!service.adminExists()) {
            System.out.println("No existe administrador. Creando uno nuevo:");
            System.out.print("Usuario admin: ");
            String adminUser = sc.nextLine();
            System.out.print("Contraseña: ");
            String adminPass = sc.nextLine();
            service.createUser(adminUser, adminPass, true);
            System.out.println("✅ Administrador creado.");
        }

        System.out.println("\n--- LOGIN ---");
        System.out.print("Usuario: ");
        String username = sc.nextLine();
        System.out.print("Contraseña: ");
        String password = sc.nextLine();

        User user = service.authenticate(username, password);
        if (user == null) {
            System.out.println("❌ Credenciales inválidas.");
            return;
        }

        if (user.isAdmin()) adminMenu(service, username, sc);
        else userMenu(service, username, sc);
    }

    private static void adminMenu(UserService service, String admin, Scanner sc) {
        while (true) {
            System.out.println("\n--- MENÚ ADMIN ---");
            System.out.println("1) Listar usuarios");
            System.out.println("2) Crear usuario");
            System.out.println("3) Eliminar usuario");
            System.out.println("4) Resetear contraseña");
            System.out.println("5) Cerrar sesión");
            System.out.print("Opción: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1" -> service.listUsers();
                case "2" -> {
                    System.out.print("Nuevo usuario: ");
                    String user = sc.nextLine();
                    System.out.print("Contraseña: ");
                    String pass = sc.nextLine();
                    service.createUser(user, pass, false);
                    System.out.println("✅ Usuario creado.");
                }
                case "3" -> {
                    System.out.print("Usuario a eliminar: ");
                    service.deleteUser(sc.nextLine());
                    System.out.println("🗑️ Usuario eliminado.");
                }
                case "4" -> {
                    System.out.print("Usuario a resetear: ");
                    service.resetPassword(sc.nextLine());
                    System.out.println("🔄 Contraseña puesta en blanco.");
                }
                case "5" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void userMenu(UserService service, String username, Scanner sc) {
        while (true) {
            System.out.println("\n--- MENÚ USUARIO ---");
            System.out.println("1) Cambiar contraseña");
            System.out.println("2) Cerrar sesión");
            System.out.print("Opción: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1" -> {
                    System.out.print("Nueva contraseña: ");
                    String newPass = sc.nextLine();
                    service.changePassword(username, newPass);
                    System.out.println("✅ Contraseña actualizada.");
                }
                case "2" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}
