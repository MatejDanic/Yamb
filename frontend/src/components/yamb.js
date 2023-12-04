import React, { Component } from 'react';
import Game from './game/game';
import AuthService from '../api/auth-service';
import GameService from '../api/game-service';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './yamb.css';

class Yamb extends Component {

    constructor(props) {
		super(props);
		this.state = {
            username: "",
			currentUser: AuthService.getCurrentPlayer(),
        }
        this.handleRollDice = this.handleRollDice.bind(this);
        this.handleRestart = this.handleRestart.bind(this);
        this.handleFillBox = this.handleFillBox.bind(this);
        this.handleMakeAnnouncement = this.handleMakeAnnouncement.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleStart = this.handleStart.bind(this);
        this.play = this.play.bind(this);
    }

    componentDidMount() {
        if (this.state.currentUser) {   
            this.play();
        }
	}

    handleStart() {
        AuthService.createTempPlayer({ 
            username: this.state.username
        })
        .then((player) => {
            localStorage.setItem("player", JSON.stringify(player)); 
            this.setState({ currentUser: player }, () => {
                this.play();
            });
        })
        .catch((error) => {
            this.handleError(error.message); 
        });
    }    

    handleFinish(totalSum) {
        toast(
            <div>
                <h3>Congratulations!</h3><h4>Your final score is:</h4><h2>{totalSum}</h2>
                <button onClick={this.play} className="new-game-button">New Game</button>
            </div>, {
                position: "top-center",
                autoClose: false,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                pauseOnFocusLoss: true,
                draggable: true,
                progress: undefined,
                theme: "dark"
        });
    }

    play() {
        GameService.play()
        .then((data) => {
            console.log(data);
            this.setState({ game: data });
            if (data.status === "FINISHED") {
                this.handleFinish(0);
            }
        })
        .catch((error) => {
            AuthService.logout();
            this.handleError(error.message);
        });
    }

    handleRollDice(diceToRoll) {
        GameService.rollDiceById(
            this.state.game.id, 
            diceToRoll
        )
        .then((data) => {
            console.log(data);
            this.setState({ game: data });
        })
        .catch((error) => {
            this.handleError(error.message);
        });
    }

    handleFillBox(columnType, boxType) {
        GameService.fillBoxById(
            this.state.game.id, columnType, boxType
        )
        .then((data) => {
            console.log(data);
            this.setState({ game: data });
        })
        .catch((error) => {
            this.handleError(error.message);
        });
    }

    handleMakeAnnouncement(type) {
        GameService.makeAnnouncementById(
            this.state.game.id, type
        )
        .then((data) => {
            console.log(data);
            this.setState({ game: data });
        })
        .catch((error) => {
            this.handleError(error.message);
        });

    }

    handleRestart() {
        GameService.restartById(
            this.state.game.id
        )
        .then((data) => {
            console.log(data);
            this.setState({ game: data });
        })
        .catch((error) => {
            this.handleError(error.message);
        });
    }

    handleLogout() {
        AuthService.logout();
    }

    handleChange(event) {
        this.setState({ username: event.target.value });
    }

    handleError(message) {
        console.error(message);
        toast.error(message, {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            pauseOnFocusLoss: true,
            draggable: true,
            progress: undefined,
            theme: "dark"
        });
    }

    render() {
        let currentUser = this.state.currentUser;
        let game = this.state.game;
        let username = this.state.username;
        let playDisabled = !username;
        return (
            <div className="yamb">
                {!currentUser && 
                    <div>
                        <input className="username-input" type="text" value={username} onChange={this.handleChange} placeholder="Name..."/>
                        <br/>
                        <button className="play-button" type="submit" disabled={playDisabled} onClick={this.handleStart}>Play</button>
                    </div>}
                {game && 
                <Game 
                    sheet={game.sheet}
                    dices={game.dices}
                    rollCount={game.rollCount}
                    announcement={game.announcement}
                    announcementRequired={game.announcementRequired}
                    topSectionSum={game.topSectionSum}
                    middleSectionSum={game.middleSectionSum}
                    bottomSectionSum={game.bottomSectionSum}
                    totalSum={game.totalSum}
                    player={game.player}
                    onRollDice={this.handleRollDice}
                    onFillBox={this.handleFillBox}
                    onMakeAnnouncement={this.handleMakeAnnouncement}
                    onRestart={this.handleRestart}
                    onLogout={this.handleLogout}>
                </Game>}
                <ToastContainer limit={5} style={{ fontSize: "14px"}}/>
            </div>
        );
    }
    
}

export default Yamb;