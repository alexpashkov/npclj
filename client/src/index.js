import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import './index.css';

const Puzzle = ({children}) => <div className="puzzle">{children.map(row => <Row key={row.join()} tiles={row}/>)}</div>;
const Row = ({tiles}) => <div className="row">{tiles.map(val => <Tile key={val} val={val}/>)}</div>;
const Tile = ({val}) => <div className="tile">{!!val && val}</div>

class App extends Component {
    render() {
        return (
            <div>
                <Puzzle>{[
                    [1, 2, 3],
                    [8, 0, 4],
                    [7, 6, 5],
                ]}</Puzzle>
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));
