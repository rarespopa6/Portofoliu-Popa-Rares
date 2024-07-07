import React, { useState, useRef, useEffect } from "react";
import AddIcon from "@mui/icons-material/Add";
import { Fab } from "@mui/material";
import { Zoom } from "@mui/material";
import { Carousel } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; 

function CreateArea() {
  return (
    <div className="mid">
      <Carousel className="custom-carousel">
        <Carousel.Item>
          <img
            className="d-block w-100 custom-image"
            src="/imgs/thrid.jpg"
            alt="First slide"
          />
          <Carousel.Caption>
            <h3>Discover. Smile. Shop.</h3>
            <p>We have an entire collection of items that waits on you.</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img
            className="d-block w-100 custom-image"
            src="/imgs/fast.png"
            alt="Second slide"
          />
          <Carousel.Caption>
            <h3>Fast, Easy and Reliable.</h3>
            <p>Our clothes and accessories have the best prices.</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img
            className="d-block w-100 custom-image"
            src="/imgs/delivery.jpg"
            alt="Third slide"
          />
          <Carousel.Caption>
            <h3>Fast Delivery.</h3>
            <p>Free shipping for orders above 200 Lei.</p>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>
    </div>
  );
}


export default CreateArea;
