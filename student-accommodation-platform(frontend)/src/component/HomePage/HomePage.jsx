import React, { useState } from "react";
import RoomResult from "../Common/RoomResult";
import RoomSearch from "../Common/RoomSearch";



const HomePage = () => {
  const [roomSearchResults, setRoomSearchResults] = useState([]);

  // Function to handle search results
  const handleSearchResult = (results) => {
    setRoomSearchResults(results);
  };

  return (
    <div className="home">
      {/* HEADER / BANNER ROOM SECTION */}
       <section>
        <header className="header-banner">
          <img
            src="./assets/images/images.jpeg"
            alt="Student Accommodation Platform"
            className="header-image"
          />
          <div className="overlay"></div>
          <div className="animated-texts overlay-content">
            <h1>
              Welcome to <span className="phegon-color">Student Accommodation Platform</span>
            </h1>
            <br />
            <h3>Step into a haven of comfort and care</h3>
          </div>
        </header>
       </section> 





      {/* SEARCH/FIND AVAILABLE ROOM SECTION */}
      <RoomSearch handleSearchResult={handleSearchResult} />
      <RoomResult roomSearchResults={roomSearchResults} />

      <h4>
        <a className="view-rooms-home" href="/rooms">
          All Rooms
        </a>
      </h4>

      <h2 className="home-services">
        Services at <span className="phegon-color">Student Accommodation Platform</span>
      </h2>

      {/* SERVICES SECTION */}
      <section className="service-section">
        <div className="service-card">
          <img src="./assets/images/ac.png" alt="Air Conditioning" />
          <div className="service-details">
            <h3 className="service-title">Air Conditioning</h3>
            <p className="service-description">
              Stay cool and comfortable throughout your stay with our
              individually controlled in-room air conditioning.
            </p>
          </div>
        </div>
        <div className="service-card">
          <img src="./assets/images/furnished.jpeg" alt="Mini Bar" />
          <div className="service-details">
            <h3 className="service-title">Furnished</h3>
            <p className="service-description">
            Convenient fully furnished flats in your local area come equipped with essential furniture, including sofas, refrigerator, microwave,  and dishwasher, ensuring a comfortable and hassle-free living experience for students.
            </p>
          </div>
        </div>
        <div className="service-card">
          <img src="./assets/images/parking.png" alt="Parking" />
          <div className="service-details">
            <h3 className="service-title">Parking</h3>
            <p className="service-description">
            There is designated parking available, specifically allotted for your flat, ensuring a secure and convenient space for your vehicle. This eliminates the hassle of searching for parking and provides added convenience for residents.
            </p>
          </div>
        </div>
        <div className="service-card">
          <img src="./assets/images/wifi.png" alt="WiFi" />
          <div className="service-details">
            <h3 className="service-title">WiFi</h3>
            <p className="service-description">
              Stay connected throughout your stay with complimentary high-speed
              Wi-Fi access available in all guest rooms and public areas.
            </p>
          </div>
        </div>
      </section>
      {/* AVAILABLE ROOMS SECTION */}
      <section></section>
    </div>
  );
};

export default HomePage;
