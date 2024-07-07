import React, { useState } from 'react';

function SizeButton({ onSizeSelect, category }){
  const [selectedSize, setSelectedSize] = useState('');

  const handleSizeChange = (event) => {
    const size = event.target.value;
    setSelectedSize(size);
    onSizeSelect(size);
  };

  return (
    <div className="size-button-container">
      {category != 'shoes' ? (
      <select 
        className="size-dropdown" 
        value={selectedSize} 
        onChange={handleSizeChange}
      >
        <option value="" disabled>Select Size</option>
        <option value="S">S</option>
        <option value="M">M</option>
        <option value="L">L</option>
        <option value="XL">XL</option>
      </select>
      ) : (
        <select 
        className="size-dropdown" 
        value={selectedSize} 
        onChange={handleSizeChange}
      >
          <option value="" disabled>Select Size</option>
          <option value="37">37</option>
          <option value="38">38</option>
          <option value="39">39</option>
          <option value="40">40</option>
          <option value="41">41</option>
          <option value="42">42</option>
          <option value="43">43</option>
          <option value="44">44</option>
          <option value="44">45</option>
        </select>
      )}
    </div>
  );
};

export default SizeButton;
