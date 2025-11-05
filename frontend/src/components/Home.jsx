import React from 'react';
import Hero from './Hero';
import FeaturedMovies from './FeaturedMovies';

const Home = () => {
    return (
        <>
            <div className="hero-wrapper">
                <div className="container">
                    <Hero />
                </div>
            </div>
            <div className="container">
                <FeaturedMovies />
            </div>
        </>
    );
};

export default Home;

