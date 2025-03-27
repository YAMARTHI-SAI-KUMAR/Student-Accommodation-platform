import React, { useState, useEffect } from 'react';
import ApiService from '../../Services/ApiService';
import Pagination from '../Common/Pagination';
import RoomResult from '../Common/RoomResult';
import RoomSearch from '../Common/RoomSearch';

const RoomsPage = () => {
  const [rooms, setRooms] = useState([]); // Ensures default value is an empty array
  const [filteredRooms, setFilteredRooms] = useState([]); 
  const [roomTypes, setRoomTypes] = useState([]);
  const [selectedRoomType, setSelectedRoomType] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [roomsPerPage] = useState(5);

  /** Fetch all rooms from API */
  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await ApiService.getAllRooms();
        console.log('Fetched Rooms:', response); // Debugging log
    
        if (response?.data && Array.isArray(response.data)) {
          setRooms(response.data); // Use response.data instead of response.roomList
          setFilteredRooms(response.data);
        } else {
          console.error('Unexpected response format:', response);
          setRooms([]);
          setFilteredRooms([]);
        }
      } catch (error) {
        console.error('Error fetching rooms:', error.message);
        setRooms([]);
        setFilteredRooms([]);
      }
    };
    

    const fetchRoomTypes = async () => {
      try {
        const types = await ApiService.getRoomTypes();
        console.log('Fetched Room Types:', types); // Debugging Log

        if (Array.isArray(types)) {
          setRoomTypes(types);
        } else {
          console.error('Unexpected room types response:', types);
          setRoomTypes([]);
        }
      } catch (error) {
        console.error('Error fetching room types:', error.message);
        setRoomTypes([]);
      }
    };

    fetchRooms();
    fetchRoomTypes();
  }, []);

  /** Handles search result from RoomSearch */
  const handleSearchResult = (results) => {
    console.log('Search Results:', results); // Debugging Log
    setRooms(results);
    setFilteredRooms(results);
  };

  /** Handles filter by room type */
  const handleRoomTypeChange = (e) => {
    setSelectedRoomType(e.target.value);
    filterRooms(e.target.value);
  };

  /** Filters rooms based on selected room type */
  const filterRooms = (type) => {
    if (type === '') {
      setFilteredRooms(rooms);
    } else {
      const filtered = rooms.filter((room) => room.roomType === type);
      setFilteredRooms(filtered);
    }
    setCurrentPage(1);
  };

  /** Pagination Logic */
  const indexOfLastRoom = currentPage * roomsPerPage;
  const indexOfFirstRoom = indexOfLastRoom - roomsPerPage;
  const currentRooms = (filteredRooms || []).slice(indexOfFirstRoom, indexOfLastRoom);

  return (
    <div className='all-rooms'>
      <h2>All Rooms</h2>
      <div className='all-room-filter-div'>
        <label>Filter by Room Type:</label>
        <select value={selectedRoomType} onChange={handleRoomTypeChange}>
          <option value="">All</option>
          {roomTypes.map((type) => (
            <option key={type} value={type}>
              {type}
            </option>
          ))}
        </select>
      </div>
      
      <RoomSearch handleSearchResult={handleSearchResult} />
      <RoomResult roomSearchResults={currentRooms} />

      <Pagination
        roomsPerPage={roomsPerPage}
        totalRooms={filteredRooms.length}
        currentPage={currentPage}
        paginate={setCurrentPage}
      />
    </div>
  );
};

export default RoomsPage;
