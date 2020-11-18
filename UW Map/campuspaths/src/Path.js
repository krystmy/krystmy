import React, {Component} from 'react';

/*this class is a component of the app, handles the dropdown menus and search/clear
buttons */
class Path extends Component {

    constructor(props) {
        super(props);
        this.state = {
            list: [],//list of building names
            to: "BAG",//building to search path to
            from: "BAG"//building to search path from
        }
    }

    //sets the value of the dropdown menu ot the one the client choose
    handleChange2 = (event) => {
        this.props.onChange2(event);
        this.setState( {
            to: event.target.value
        })
    };

    //sets the value of the dropdown menu ot the one the client choose
    handleChange1 = (event) => {
        this.props.onChange1(event);
        this.setState( {
            from: event.target.value
        })
    };

    //searches for path when button is clicked
    onClick = () => {
        this.props.search();
    };

    //clears the path drawn and resets the dropdown bar
    onClear = () => {
        this.props.clear();
    };

    //creates a dropdown list of all uw buildings
    dropdown = (list) => {
        let ret = [];
        for(let index = 0; index < list.length; index++) {
            ret.push(<option value={list[index][0]}>{list[index][1]} </option>);
        }
        return ( ret )
    };

    render() {
        return (
            <div id="path">
                <p style={{color: 'red'}}> Starting:
                    <select value={this.props.from} onChange={this.handleChange1}>
                        {this.dropdown(this.props.value)}
                    </select>
                </p>
                <p style={{color: 'green'}}> Destination:
                    <select value={this.props.to} onChange={this.handleChange2}>
                        {this.dropdown(this.props.value)}
                    </select>

                    <button style={{marginLeft: "10px", marginRight: "10px"}} onClick={this.onClick}>Search</button>
                    <button onClick={this.onClear}>Clear</button>
                </p>
            </div>
        );
    }
}

export default Path;
