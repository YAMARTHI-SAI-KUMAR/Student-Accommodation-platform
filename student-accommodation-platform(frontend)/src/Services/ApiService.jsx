import axios from "axios";

export default class ApiService {
    static BASE_URL = "http://localhost:4040";

    static getHeader(useAuth = true) {
        const token = localStorage.getItem("token");
        const headers = { "Content-Type": "application/json" };
        if (useAuth && token) {
            headers.Authorization = `Bearer ${token}`;
        }
        return headers;
    }

   // Add request timeout
   static async request(method, url, data = null) {
    try {
        console.log('Making request:', method, url, data); // Log request details
        const response = await axios({
            method,
            url: `${this.BASE_URL}${url}`,
            data,
            headers: this.getHeader(),
        });
        console.log('API response:', response.data); // Log response
        return response.data;
    } catch (error) {
        console.error(`API Error (${method.toUpperCase()} ${url}):`, error); // Already present
        throw error.response?.data || { message: "Something went wrong" };
    }
}
    /*** AUTH ***/
    static registerUser(registration) {
        return this.request("post", "/auth/register", registration);
    }

    static loginUser(loginDetails) {
        return this.request("post", "/auth/login", loginDetails);
    }

    /*** USERS ***/
    static getAllUsers() {
        return this.request("get", "/users/all");
    }

    static getUserProfile() {
        return this.request("get", "/users/get-logged-in-profile-info");
    }

    static getUser(userId) {
        return this.request("get", `/users/get-by-id/${userId}`);
    }

    static getUserBookings(userId) {
        return this.request("get", `/users/get-user-bookings/${userId}`);
    }

    static deleteUser(userId) {
        return this.request("delete", `/users/delete/${userId}`);
    }

    /*** ROOMS ***/
    static addRoom(formData) {
        return this.request("post", "/rooms/add", formData);
    }

    static getAllAvailableRooms() {
        return this.request("get", "/rooms/all-available-rooms");
    }

    static getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType) {
        return this.request(
            "get",
            `/rooms/available-rooms-by-date-and-type?checkInDate=${encodeURIComponent(
                checkInDate
            )}&checkOutDate=${encodeURIComponent(
                checkOutDate
            )}&roomType=${encodeURIComponent(roomType)}`
        );
    }

    static async getRoomTypes() {
        try {
          const response = await this.request('get', '/rooms/types');
          return Array.isArray(response) ? response : response?.roomTypes || [];
        } catch (error) {
          console.error('Error fetching room types:', error);
          return [];
        }
      }
      

    static getAllRooms() {
        return this.request("get", "/rooms/all");
    }

    static getRoomById(roomId) {
        return this.request("get", `/rooms/room-by-id/${roomId}`);
    }

    static deleteRoom(roomId) {
        return this.request("delete", `/rooms/delete/${roomId}`);
    }

    static updateRoom(roomId, formData) {
        return this.request("put", `/rooms/update/${roomId}`, formData);
    }

    /*** BOOKINGS ***/
    static async bookRoom(roomId, userId, booking) {
        if (!userId) {
            console.error("Error: User ID is missing.");
            return;
        }
        return this.request(
            "post",
            `/bookings/book-room/${roomId}/${userId}`,
            booking
        );
    }

    static getAllBookings() {
        return this.request("get", "/bookings/all");
    }

    static getBookingByConfirmationCode(bookingCode) {
        return this.request(
            "get",
            `/bookings/get-by-confirmation-code/${bookingCode}`
        );
    }

    static cancelBooking(bookingId) {
        return this.request("delete", `/bookings/cancel/${bookingId}`);
    }

    /*** AUTH CHECKS ***/
    static logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    static isAuthenticated() {
        return !!localStorage.getItem("token");
    }

    static isAdmin() {
        return localStorage.getItem("role") === "ADMIN";
    }

    static isUser() {
        return localStorage.getItem("role") === "USER";
    }
}
