import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Puzzle from "./Puzzle";


class App extends Component {
    state = {
        pzl: [
            [1, 2, 3],
            [8, 0, 4],
            [7, 6, 5],
        ],
        size: 4
    };

    onSizeChange = ({currentTarget: {value}}) => this.setState({size: value});
    solve = () => {
        fetch('http://localhost:3000', {
            method: 'POST',
            body: JSON.stringify({puzzle: this.state.pzl})
        }).then(d => d.text()).then(console.log)
    }

    render() {
        const {pzl, size} = this.state;
        return (
            <div>
                <button>Generate</button>
                <input type="number" min="3" value={size} onChange={this.onSizeChange}/>
                <Puzzle>{pzl}</Puzzle>
                <button onClick={this.solve}>Solve!</button>
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));
