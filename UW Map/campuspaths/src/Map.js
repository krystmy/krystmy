/*
 * Copyright ©2019 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Summer Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import "./Map.css";

/* a map of the uw campus, and draws the shortest path from one building
to another one*/
class Map extends Component {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    constructor(props) {
        super(props);
        this.state = {
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    //draw background image and the shortest path as requested
    drawBackgroundImage() {
        let canvas = this.canvas.current;
        let ctx = canvas.getContext("2d");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        this.drawPath();
    }

    //draws the shortest path from start building to destination
    drawPath = () =>  {
        let canvas = this.canvas.current;
        let ctx = canvas.getContext("2d");
        let size = this.props.pathList.length;
        let coord = this.props.pathList;
        if(this.props.pathList[0] !== undefined) {
            this.drawCircle(ctx, coord[0], "red");
            this.drawCircle(ctx, coord[size - 1], "chartreuse");
        }
        ctx.lineWidth = 10;
        ctx.strokeStyle = "blue";
        ctx.beginPath();
        for(let i = 0; i < size; i += 2) {
            ctx.moveTo(coord[i][0], coord[i][1]);
            ctx.lineTo(coord[i + 1][0], coord[i + 1][1]);
            ctx.stroke();
        }
    };

    //draws a circle indicating the selected building
    drawCircle = (ctx, coord, color) => {
        ctx.fillStyle = color;
        let radius = 18;
        ctx.beginPath();
        ctx.arc(coord[0], coord[1], radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    render() {
        return (
            <canvas ref={this.canvas}/>
        )
    }
}

export default Map;