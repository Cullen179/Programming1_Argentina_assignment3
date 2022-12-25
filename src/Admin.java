import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Admin {
    // This method is used to log in for admin role.
    public static boolean login() throws IOException {
        System.out.println("\nLOG IN AS ADMIN ROLE");
        // Create a scanner object to be ready to get input information (username & password) from users via keyboard.
        Scanner sc = new Scanner(System.in);
        String usernameAd;
        System.out.print("Please type your username: ");
        while (true) {
            usernameAd = sc.nextLine();
            if (checkAdminUsernameExisted(usernameAd)) break;
            else {
                System.out.println("This username is not existed. Please try with another one.");
            }
        }
        String passwordAd;
        System.out.print("Please type your password: ");
        while (true) {
            passwordAd = sc.nextLine();
            if (checkAdminPasswordExisted(passwordAd)) break;
            else {
                System.out.println("This password is not existed. Please try with another one.");
            }
        }
        // Create a scanner object to read from an admin text file.
        Scanner scannerAdmin = new Scanner(new File("./src/File/admin.txt"));
        // Continue to loop through each line of admin.txt file to find the username and password of admin.
        while (scannerAdmin.hasNextLine()) {
            String currentAdmin = scannerAdmin.nextLine();
            String[] currentAdminInfo = currentAdmin.split(",");
            String currentAdminUsername = currentAdminInfo[0];
            String currentAdminPassword = currentAdminInfo[1];
            // in case the users input are matched, completed this function.
            if (currentAdminUsername.equals(usernameAd) && currentAdminPassword.equals(passwordAd)) {
                // Prompt user a successful message
                System.out.println("LOGIN SUCCESSFULLY");
                System.out.println("-------------------");
                scannerAdmin.close();
                return true;
            }
        }
        // In case the users input are not matched, prompt user an unsuccessful message.
        System.out.println("Login unsuccessfully, please check your username and password and try again");
        scannerAdmin.close();
        return false;
    }
    public static boolean checkAdminUsernameExisted(String username) throws IOException {
        Scanner scannerAdmin = new Scanner(new File("./src/File/admin.txt"));

        while (scannerAdmin.hasNextLine()) {
            String currentAdmin = scannerAdmin.nextLine();
            String[] currentAdminInfo = currentAdmin.split(",");
            String currentAdminUsername = currentAdminInfo[0];

            if (username.equals(currentAdminUsername)) {
                return true;
            }
        }
        scannerAdmin.close();
        return false;
    }

    public static boolean checkAdminPasswordExisted(String password) throws IOException {
        Scanner scannerAdmin = new Scanner(new File("./src/File/admin.txt"));

        while (scannerAdmin.hasNextLine()) {
            String currentAdmin = scannerAdmin.nextLine();
            String[] currentAdminInfo = currentAdmin.split(",");
            String currentAdminPassword = currentAdminInfo[1];

            if (password.equals(currentAdminPassword)) {
                return true;
            }
        }
        scannerAdmin.close();
        return false;
    }

    // This method is used to view products for admin.
    public static void viewProducts() throws IOException {
        // Create a scanner object to read from an item text file.
        Scanner scannerProduct = new Scanner(new File("./src/File/items.txt"));
        System.out.println("\nVIEW PRODUCT\n");
        // A loop is used to display detailed information of each product.
        while (scannerProduct.hasNextLine()) {
            String items = scannerProduct.nextLine();
            Product.productDetails(items);
        }
        scannerProduct.close();
    }

    // This method is used to view orders for admin.
    public static void viewOrders() throws IOException {
        // Create a scanner object to read from an item text file.
        Scanner scannerOrder = new Scanner(new File("./src/File/order.txt"));
        System.out.println("\nVIEW ORDER");
        // A loop is used to display detailed information of each order.
        while (scannerOrder.hasNextLine()) {
            String order = scannerOrder.nextLine();
            Order.orderDetail(order);
        }
        scannerOrder.close();
    }

    public static void viewMembers() throws IOException {
        // Create a scanner object to read from a member text file.
        Scanner scannerMember = new Scanner(new File("./src/File/member.txt"));
        System.out.println("\nVIEW MEMBER");
        // A loop is used to display detailed information of each member.
        while (scannerMember.hasNextLine()) {
            String member = scannerMember.nextLine();
            // ADD THIS FUNCTION IN MEMBER CLASS LATER
//            Member.memberDetail(member);
        }
        scannerMember.close();

    }

    // This method is used to add new product for admin.
    public static void addProduct() throws IOException{
        System.out.println("\nADD NEW PRODUCT");
        PrintWriter pw = new PrintWriter(new FileWriter("src/File/items.txt", true));
        Product newProduct = Product.createProduct();
        pw.printf("\n%s,%s,%.1f,%s", newProduct.getId(), newProduct.getName(), newProduct.getPrice(), newProduct.getCategory());
        System.out.println("ADD PRODUCT SUCCESSFULLY");
        pw.close();
    }
    public static void removeProduct() throws IOException {
        System.out.println("\nREMOVE PRODUCT");
        // Create a scanner object to be ready to get input information (nameItem) from users via keyboard.
        Scanner scannerInput = new Scanner(System.in);
        System.out.print("Enter item's name: ");
        String nameItemInput = scannerInput.nextLine();
        // Create a scanner object to read from an item text file.
        Scanner scannerProduct = new Scanner(new File("./src/File/items.txt"));

        // Create a writer for a temporary file to store updated data
        File inputFile = new File("./src/File/items.txt");
        File tempFile = new File("tempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        // A boolean value to check the item name matched or not.
        boolean checkItem = false;

        while (scannerProduct.hasNextLine()) {
            String currentItem = scannerProduct.nextLine();
            String[] currentItemInfo = currentItem.split(",");
            String currentItemName = currentItemInfo[1];
            // In case the name from input is not equivalent to the name of item in the file, it would add the new line.
            if (!nameItemInput.equals(currentItemName)) {
                writer.write(currentItem + (scannerProduct.hasNextLine() ? System.lineSeparator() : ""));
                continue;
            }
            checkItem = true;
            String removeName = nameItemInput;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Trim the newline when comparing with removeName.
                String trimmedLine = currentLine.trim();
                // In case the trimmedLine contains the name of removed product, it would be deleted.
                if (trimmedLine.equals(removeName)) {
                    writer.write((scannerProduct.hasNextLine() ? System.lineSeparator() : ""));
                }
            }
            // Prompt a message to users.
            System.out.println("The product has been successfully removed.\n");
        }
        // Rename the temporary file to the original one.
        tempFile.renameTo(new File("./src/File/items.txt"));
        writer.close();
        // In case an item is not found, it would prompt user an unsuccessful message.
        if (!checkItem) {
            System.out.println("This item's name cannot found. Please try with another one.\n");
        }
    }

    // This method is used to update the price of product for admin.
    public static void updatePrice() throws IOException {
        System.out.println("\nUPDATE THE PRICE OF PRODUCT");
        // Create a scanner object to be ready to get input information (nameItem) from users via keyboard.
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter item's name: ");
        String nameInput = sc.nextLine();
        // Create a scanner object to read from an item text file.
        Scanner scannerProduct = new Scanner(new File("./src/File/items.txt"));
        // Create a writer for a temporary file to store updated data
        File tempFile = new File("tempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        // A boolean value to check the item's name existed or not.
        boolean checkItem = false;
        // Continue to loop through each line of items.txt file to find the name of item.
        while (scannerProduct.hasNextLine()) {
            String currentItem = scannerProduct.nextLine();
            String[] currentItemInfo = currentItem.split(",");
            String currentItemName = currentItemInfo[1];
            // In case the name from input is not equivalent to the name of item in the file, it would add the new line.
            if (!nameInput.equals(currentItemName)) {
                writer.write(currentItem + (scannerProduct.hasNextLine() ? System.lineSeparator() : ""));
                continue;
            }
            // In case an item is found, it would prompt user to input the new price of item.
            checkItem = true;
            String updatePrice = String.valueOf(InputValidator.validateDoubleInput(
                    "Type the new item's price: ",
                    "The price should be a natural or decimal number. Please try again."));
            // Updated the new price for item.
            String itemPrice = currentItem.split(",")[2];
            String updateItem = currentItem.replace(itemPrice, updatePrice);
            // Write the updated line to the temporary file.
            writer.write(updateItem + (scannerProduct.hasNextLine() ? System.lineSeparator() : ""));
            // Prompt a message to users.
            System.out.println("Update the price of product successfully");
        }
        // Rename the temporary file to the original one.
        tempFile.renameTo(new File("./src/File/items.txt"));
        // In case an item is not found, it would prompt user an unsuccessful message.
        if (!checkItem) {
            System.out.println("\nThis item's name cannot found. Please try with another one.");
        }
        writer.close();
    }
    public static void addCategory() {

    }

    public static void removeCategory() {

    }
    // This method is used to get the information of all orders made by customers through his/ her ID.
    public static void getOrderByCustomerID() throws IOException {
        System.out.println("\nGET ORDER BY CUSTOMER ID");
        // Create a scanner object to be ready to get input information (customer ID) from users via keyboard.
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Customer ID: ");
        String customerIDInput = sc.nextLine();
        // Create a scanner object to read from an order text file.
        Scanner scannerOrder = new Scanner(new File("./src/File/order.txt"));
        // A boolean value to check the item's name existed or not.
        boolean checkCustomerIDExisted = false;
        // Continue to loop through each line of order.txt file to find the ID of customers.
        while (scannerOrder.hasNextLine()) {
            String order = scannerOrder.nextLine();
            String[] orderInfo = order.split(",");
            String customerID = orderInfo[1];
            // In case the input ID is equivalent to the customerID from file, the following function would be executed.
            if (customerIDInput.equals(customerID)) {
                Order.orderDetail(order);
                checkCustomerIDExisted = true;
            }
        }
        // In case the customer's id is not existed, prompt user a message.
        if (!checkCustomerIDExisted) {
            System.out.println("\nThis customer's id cannot found. Please try with another one.");
        }
        scannerOrder.close();
    }

    // This method is used to change the status of an order.
    public static void changeOrderStatus() throws IOException {
        System.out.println("\nCHANGE STATUS OF THE ORDER");
        // Create a scanner object to be ready to get input information (order ID) from users via keyboard.
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the order ID: ");
        String orderIDInput = sc.nextLine();
        // Create a scanner object to read from an order text file.
        Scanner scannerOrder = new Scanner(new File("./src/File/order.txt"));
        // Create a writer for a temporary file to store updated data
        File tempFile = new File("tempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        // A boolean value to check the order was made or not.
        boolean checkOrderIDExisted = false;
        // Continue to loop through each line of order.txt file to find the ID of an order.
        while (scannerOrder.hasNextLine()) {
            String order = scannerOrder.nextLine();
            String[] orderInfo = order.split(",");
            String orderID = orderInfo[0];
            // In case the ID from input is not equivalent to the orderID in the file, it would add the new line.
            if (!orderIDInput.equals(orderID)) {
                writer.write(order + (scannerOrder.hasNextLine() ? System.lineSeparator() : ""));
                continue;
            }
            checkOrderIDExisted = true;
            String orderStatus = orderInfo[9];
            // In case the status is delivered, it would be changed to 'paid', update the new line to the file then prompt a message.
            if (!orderStatus.equals("paid")) {
                String updatedOrder = order.replace(orderStatus, "paid");
                writer.write(updatedOrder + (scannerOrder.hasNextLine() ? System.lineSeparator() : ""));
                System.out.println("The status of this order has been successfully changed to paid.\n");
            } // In case the status is paid, it would prompt a message.
            else {
                System.out.println("This order is already paid received.");
                writer.write(order + (scannerOrder.hasNextLine() ? System.lineSeparator() : ""));
                continue;
            }
            // Rename the temporary file to the original one.
            tempFile.renameTo(new File("./src/File/order.txt"));
            writer.close();
            }
        // In case the order's id is not existed, prompt user a message.
        if (!checkOrderIDExisted) {
            System.out.println("\nThis order's id cannot found. Please try with another one.");
        }
        scannerOrder.close();
    }

    public static void listProductWithHighestQuantity() throws IOException {
        System.out.println("\nLIST THE PRODUCT WITH THE HIGHEST QUANTITY BOUGHT BY A CUSTOMER/ MEMBER");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the customer ID: ");
        String cusID = sc.nextLine();
    }
    public static boolean validateDateInput(String date) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner scannerOrder = new Scanner(new File("./src/File/order.txt"));
        try {
            formatter.parse(date);
            double revenue = 0;
            while (scannerOrder.hasNextLine()) {
                String order = scannerOrder.nextLine();
                String[] orderInfo = order.split(",");
                String orderDate = orderInfo[4];

                if (orderDate.equals(date)) {
                    double orderTotal = Double.parseDouble(orderInfo[8]);
                    revenue += orderTotal;
                }
            }
            System.out.printf("\nThe total revenue in %s is %.2f", date, revenue);
        }
        catch (Exception e) {
            System.out.println("\nInvalid input, please try again.");
        }
        return true;
    }
    public static void calculateTotalRevenue() throws IOException {
        System.out.println("\nCALCULATE THE STORE TOTAL REVENUE IN A PARTICULAR DAY");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the date that you want to calculate the revenue: ");
        String date = sc.nextLine();
        validateDateInput(date);
    }

    public static void checkOrderInfoInADay() throws IOException {
        System.out.println("\nCHECK THE INFORMATION OF ALL ORDERS EXECUTED IN A PARTICULAR DAY");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the date that you want to check info of all orders: ");
    }

    public static void main(String[] args) throws IOException {
//        login();
        calculateTotalRevenue();
    }
}

