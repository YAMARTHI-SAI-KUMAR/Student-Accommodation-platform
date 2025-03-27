import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import ApiService from '../../Services/ApiService';

const RoomSearch = ({ handleSearchResult }) => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [roomType, setRoomType] = useState('');
  const [roomTypes, setRoomTypes] = useState([]); // Ensure it's always an array
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchRoomTypes = async () => {
      try {
        const response = await ApiService.getRoomTypes();
        console.log('Room Types API Response:', response); // Debugging log

        if (Array.isArray(response)) {
          setRoomTypes(response);
        } else if (response?.roomTypes && Array.isArray(response.roomTypes)) {
          setRoomTypes(response.roomTypes);
        } else {
          console.error('Unexpected API response format for room types:', response);
          setRoomTypes([]); // Fallback to empty array
        }
      } catch (error) {
        console.error('Error fetching room types:', error.message);
        setRoomTypes([]); // Prevent .map() errors
      }
    };
    fetchRoomTypes();
  }, []);

  /** Method to show errors */
  const showError = (message, timeout = 5000) => {
    setError(message);
    setTimeout(() => {
      setError('');
    }, timeout);
  };

  /** Fetch available rooms based on search data */
  const handleInternalSearch = async () => {
    if (!startDate || !endDate || !roomType) {
      showError('Please select all fields');
      return;
    }

    try {
      const formattedStartDate = startDate.toISOString().split('T')[0];
      const formattedEndDate = endDate.toISOString().split('T')[0];

      const response = await ApiService.getAvailableRoomsByDateAndType(
        formattedStartDate,
        formattedEndDate,
        roomType
      );

      console.log('Available Rooms Response:', response); // Debugging log

      if (response?.statusCode === 200) {
        if (response.roomList.length === 0) {
          showError('No rooms available for this date range and room type.');
          return;
        }
        handleSearchResult(response.roomList);
        setError('');
      } else {
        showError('Unexpected response from server.');
      }
    } catch (error) {
      console.error('Error fetching available rooms:', error);
      showError('Unknown error occurred. Please try again.');
    }
  };

  return (
    <section>
      <div className="search-container">
        <div className="search-field">
          <label>Check-in Date</label>
          <DatePicker
            selected={startDate}
            onChange={(date) => setStartDate(date)}
            dateFormat="dd/MM/yyyy"
            placeholderText="Select Check-in Date"
          />
        </div>
        <div className="search-field">
          <label>Check-out Date</label>
          <DatePicker
            selected={endDate}
            onChange={(date) => setEndDate(date)}
            dateFormat="dd/MM/yyyy"
            placeholderText="Select Check-out Date"
          />
        </div>

        <div className="search-field">
          <label>Room Type</label>
          <select value={roomType} onChange={(e) => setRoomType(e.target.value)}>
            <option disabled value="">
              Select Room Type
            </option>
            {(roomTypes || []).map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        <button className="home-search-button" onClick={handleInternalSearch}>
          Search Rooms
        </button>
      </div>

      {error && <p className="error-message">{error}</p>}
    </section>
  );
};

export default RoomSearch;
