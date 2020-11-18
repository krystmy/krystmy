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
import Map from "./Map";
import Path from "./Path";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            from: "BAG",//starting place
            to: "BAG",//destination place
            list: [],//list of building names
            pathList: []//list of coordinates from start to end building
        };
    }

    //a request made to get the list of uw buildings from the sparks server
    //and sets the state
    async places() {
        try {
            let response = await fetch("http://localhost:4567/buildings");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let object = await response.json();
            let place = [];
            for (let value of object) {
                place.push(value);
            }
            this.setState({
                list: place
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    //request to get the shortest path from start building to destination from
    //the sparks server and set the state
    async findPath() {
        try {
            let response = await fetch("http://localhost:4567/find-path?from="
                + this.state.from + "&to=" + this.state.to);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let object = await response.json();
            let location = [];
            for (let value of object.path) {
                let coord1 = [parseInt(value.start.x), parseInt(value.start.y)];
                let coord2 = [parseInt(value.end.x), parseInt(value.end.y)];
                location.push(coord1);
                location.push(coord2)
            }
            this.setState({pathList: location})
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    //To reset the app by setting it to its initial state
    clear = () => {
        this.setState({to: "BAG", from: "BAG", pathList: []});
    };

    //calls the find path method that finds the shortest path
    search = () => {
        this.findPath();
    };

    render() {
        this.places();
        return (
            <div>
                <p id="app-title">Welcome to the CampusPaths GUI! Start by choosing a starting location
                and a final destination</p>
                <Path value={this.state.list} clear={this.clear} search={this.search}
                      to={this.state.to} from={this.state.from}
                      onChange1={(event) => {this.setState({from: event.target.value});}}
                      onChange2={(event) => {this.setState({to: event.target.value});}}/>
                <Map pathList={this.state.pathList}/>
            </div>
        );
    }

}

export default App;
