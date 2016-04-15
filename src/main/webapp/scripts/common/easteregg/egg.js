/* global cheet */
/* global window */
/* global jQuery */
/* global document */
/* global Phaser */
/* global location */
'use strict';

cheet('h u r z', function() {
  function initGame() {
    window.addEventListener('keyup', function(e) {
      if (e.keyCode === 27) {
        location.reload();
      }
    }, false);

    var node = document.createElement('div');
    node.id = 'game';
    node.style.position = 'absolute';
    node.style.top = window.scrollY + 'px';
    node.style.zIndex = '999999';
    node.style.background = 'rgba(0, 0, 0, 0.5)';
    document.body.appendChild(node);

    var game = new Phaser.Game(window.innerWidth, window.innerHeight,
      Phaser.AUTO, 'game', null, true);

    var ball;
    var bricks;
    var paddle;

    var ballOnPaddle = true;

    var lives = 3;
    var score = 0;

    var scoreText;
    var bricksText;
    var livesText;
    var introText;
    var introText2;

    var divisionX = window.innerWidth / 20;
    var divisionY = window.innerHeight / 20;

    var pictureDivisionX = 1600 / 20;
    var pictureDivisionY = 1200 / 20;

    function releaseBall() {
      if (ballOnPaddle) {
        ballOnPaddle = false;
        ball.body.velocity.y = -300;
        ball.body.velocity.x = -75;
        introText.visible = false;
        introText2.visible = false;
      }
    }

    function gameOver() {
      ball.body.velocity.setTo(0, 0);
      introText.text = 'Game Over! refreshing the page...';
      introText.visible = true;
    }

    function ballLost() {
      lives--;
      livesText.text = 'lives: ' + lives;

      if (lives === 0) {
        gameOver();
        setTimeout(function() {
          location.reload();
        }, 1000);
      } else {
        ballOnPaddle = true;
        ball.reset(paddle.body.x + 16, paddle.y - 16);
        ball.animations.stop();
      }
    }

    function ballHitBrick(_ball, _brick) {
      _brick.kill();
      score += 10;
      scoreText.text = 'score: ' + score;
      bricksText.text = 'bricks left: ' + bricks.countLiving();
      //  Are they any bricks left?
      if (bricks.countLiving() === 0) {
        //  New level starts
        score += 1000;
        scoreText.text = 'score: ' + score;
        introText.text = '- Next Level -';

        //  Let's move the ball back to the paddle
        ballOnPaddle = true;
        ball.body.velocity.set(0);
        ball.x = paddle.x + 16;
        ball.y = paddle.y - 16;
        ball.animations.stop();

        //  And bring the bricks back from the dead :)
        bricks.callAll('revive');
      }
    }

    function ballHitPaddle(_ball, _paddle) {
      var diff = 0;
      if (_ball.x < _paddle.x) {
        //  Ball is on the left-hand side of the paddle
        diff = _paddle.x - _ball.x;
        _ball.body.velocity.x = (-10 * diff);
      } else if (_ball.x > _paddle.x) {
        //  Ball is on the right-hand side of the paddle
        diff = _ball.x - _paddle.x;
        _ball.body.velocity.x = (10 * diff);
      } else {
        //  Ball is perfectly in the middle
        //  Add a little random X to stop it bouncing straight up!
        _ball.body.velocity.x = 2 + Math.random() * 8;
      }
    }

    var mainState = {
      preload: function() {
        game.load.crossOrigin = 'Anonymous';
        game.load.atlas('breakout',
        'https://s3-us-west-2.amazonaws.com/demos92/game-in-page/breakout.png',
        'https://s3-us-west-2.amazonaws.com/demos92/game-in-page/' +
        'breakout.json');
        console.log(game.load.spritesheet('page', '/assets/images/egg.jpg',
          pictureDivisionX, pictureDivisionY));
      },
      create: function() {
        game.physics.startSystem(Phaser.Physics.ARCADE);

        // We check bounds collisions against
        //all walls other than the bottom one
        game.physics.arcade.checkCollision.down = false;

        bricks = game.add.group();
        bricks.enableBody = true;
        bricks.physicsBodyType = Phaser.Physics.ARCADE;

        var brick;

        for (var y = 1; y < 13; y++) {
          for (var x = 2; x < 18; x++) {
            if (Math.round(Math.random() * 100) % 4 !== 0) {
              brick = bricks.create(x * divisionX, y * divisionY, 'page',
                20 * y + x);
              brick.scale.setTo(0.99 * divisionX / pictureDivisionX,
                0.99 * divisionY / pictureDivisionY);
              brick.body.bounce.set(1);
              brick.body.immovable = true;
            }
          }
        }

        paddle = game.add.sprite(game.world.centerX, window.innerHeight - 100,
          'breakout', 'paddle_big.png');
        paddle.anchor.setTo(0.5, 0.5);

        game.physics.enable(paddle, Phaser.Physics.ARCADE);

        paddle.body.collideWorldBounds = true;
        paddle.body.bounce.set(1);
        paddle.body.immovable = true;

        ball = game.add.sprite(game.world.centerX, paddle.y - 16, 'breakout',
          'ball_1.png');
        ball.anchor.set(0.5);
        ball.checkWorldBounds = true;

        game.physics.enable(ball, Phaser.Physics.ARCADE);

        ball.body.collideWorldBounds = true;
        ball.body.bounce.set(1);

        ball.events.onOutOfBounds.add(ballLost, this);

        scoreText = game.add.text(game.world.centerX, window.innerHeight - 50,
          'score: 0', {font: '20px Arial', fill: '#ffffff', align: 'center'});
        bricksText = game.add.text(32, window.innerHeight - 50,
          'bricks left: ' + bricks.countLiving(),
         {font: '20px Arial', fill: '#ffffff', align: 'left'});
        livesText = game.add.text(window.innerWidth - 100,
          window.innerHeight - 50, 'lives: 3',
          {font: '20px Arial', fill: '#ffffff', align: 'right'});
        introText = game.add.text(game.world.centerX,
          window.innerHeight / 1.3, '- click to start -',
          {font: '40px Arial', fill: '#ffffff', align: 'center'});
        introText2 = game.add.text(game.world.centerX,
          window.innerHeight / 1.3 + 23, '- esc to exit -',
          {font: '25px Arial', fill: '#ffffff', align: 'center'});

        introText.anchor.setTo(0.5, 0.5);
        introText2.anchor.setTo(0.5, 0);
        scoreText.anchor.setTo(0.5, 0);

        game.input.onDown.add(releaseBall, this);

      },
      update: function() {
        paddle.x = game.input.x;

        if (paddle.x < 24) {
          paddle.x = 24;
        } else if (paddle.x > game.width - 24) {
          paddle.x = game.width - 24;
        }

        if (ballOnPaddle) {
          ball.body.x = paddle.x;
        } else {
          game.physics.arcade.collide(ball, paddle, ballHitPaddle, null, this);
          game.physics.arcade.collide(ball, bricks, ballHitBrick, null, this);
        }
      }
    };

    game.state.add('main', mainState);
    game.state.start('main');
  }

  jQuery.ajax({url:
    'https://cdnjs.cloudflare.com/ajax/libs/phaser/2.4.4/phaser.min.js',
        dataType: 'script',
        cache: true,
        success: function() {
          initGame();
        }
  });
});
