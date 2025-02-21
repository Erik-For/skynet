# app/controllers/public_controller.rb
class PublicController < ApplicationController
  def index
    @players = Player.all
    
    if params[:search].present?
      search_term = "%#{params[:search].downcase}%"
      @players = @players.where("LOWER(username) LIKE ? OR LOWER(nickname) LIKE ?", 
                               search_term, search_term)
    end

    @players = sort_players(@players)
    
    respond_to do |format|
      format.html
    end
  end

  def show 
    @player = Player.all
    @players = sort_players(@players)

    respond_to do |format|
      format.html
      format.turbo_stream {
        render turbo_stream: turbo_stream.replace('players', 
               partial: 'public/players', 
               locals: { players: @players })
      }
    end
  end

  private

  def sort_players(players)
    return players unless params[:sort].present?

    direction = %w[asc desc].include?(params[:direction]) ? params[:direction] : 'asc'
    
    case params[:sort]
    when "uuid", "username", "rank", "playtime", "first_join", "last_seen"
      players.order({ params[:sort] => direction })
    else
      players
    end
  end
end