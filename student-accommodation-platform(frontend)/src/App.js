

// src/App.js
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './component/Common/NavigationBar'
import FooterComponent from './component/Common/Footer'
import LoginPage from './component/Auth/LoginPage'
import RegisterPage from './component/Auth/RegisterPage'
import HomePage from './component/HomePage/HomePage'
import AllRoomsPage from './component/BookingRooms/RoomsPage'
import RoomDetailsBookingPage from './component/BookingRooms/RoomDetailsPage';
import FindBookingPage from './component/BookingRooms/FindBookingPage';

import AdminPage from './component/Admin/AdminPage';
import ManageRoomPage from './component/Admin/ManageRoomPage';
import EditRoomPage from './component/Admin/EditRoomPage';
import AddRoomPage from './component/Admin/AddRoomPage';
import ManageBookingsPage from './component/Admin/ManageBookingsPage';
import EditBookingPage from './component/Admin/EditBookingPage';
import ProfilePage from './component/Profile/ProfilePage';
import EditProfilePage from './component/Profile/EditProfilePage';
import { ProtectedRoute, AdminRoute } from './Services/ProtectedRoute';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            {/* Public Routes */}
            <Route exact path="/home" element={<HomePage />} />
            <Route exact path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/rooms" element={<AllRoomsPage />} />
            <Route path="/find-booking" element={<FindBookingPage />} />

            {/* Protected Routes */}
            <Route path="/room-details-book/:roomId"
              element={<ProtectedRoute element={<RoomDetailsBookingPage />} />}
            />
            <Route path="/profile"
              element={<ProtectedRoute element={<ProfilePage />} />}
            />
            <Route path="/edit-profile"
              element={<ProtectedRoute element={<EditProfilePage />} />}
            />

            {/* Admin Routes */}
            <Route path="/admin"
              element={<AdminRoute element={<AdminPage />} />}
            />
            <Route path="/admin/manage-rooms"
              element={<AdminRoute element={<ManageRoomPage />} />}
            />
            <Route path="/admin/edit-room/:roomId"
              element={<AdminRoute element={<EditRoomPage />} />}
            />
            <Route path="/admin/add-room"
              element={<AdminRoute element={<AddRoomPage />} />}
            />
            <Route path="/admin/manage-bookings"
              element={<AdminRoute element={<ManageBookingsPage />} />}
            />
            <Route path="/admin/edit-booking/:bookingCode"
              element={<AdminRoute element={<EditBookingPage />} />}
            />

            {/* Fallback Route */}
            <Route path="*" element={<Navigate to="/login" />} />
          </Routes>
        </div>
        <FooterComponent />
      </div>
    </BrowserRouter>
  );
}

export default App;







